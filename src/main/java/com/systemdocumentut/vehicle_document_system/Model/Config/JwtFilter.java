package com.systemdocumentut.vehicle_document_system.Model.Config;

import com.systemdocumentut.vehicle_document_system.Repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private JWTAuthtenticationConfig jwtAuthtenticationConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String authHeader = request.getHeader("Authorization");
        
        // CORRECCIÓN 1: Buscar "APIKey" (como en el resto del proyecto) o "x-api-key"
        String apiKeyHeader = request.getHeader("APIKey"); 
        if (apiKeyHeader == null) {
            apiKeyHeader = request.getHeader("x-api-key");
        }

        // 1. LISTA BLANCA ACTUALIZADA
        // Añadimos "/LaboratorioV1" para que el filtro no pida Token ni APIKey aquí
        if (path.startsWith("/auth") || 
            path.startsWith("/LaboratorioV1") || // <--- PERMITIR LABORATORIO
            path.contains("/vencidos") || 
            path.contains("/placa") || 
            path.contains("/operar") ||
            path.contains("/conteo")) {
            
            filterChain.doFilter(request, response);
            return;
        }

        // 2. VALIDACIÓN DE APIKEY (Para el resto de rutas privadas)
        if (apiKeyHeader == null || !usuarioRepo.existsByApikey(apiKeyHeader)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"APIKey invalida o ausente.\"}");
            return;
        }

        // 3. VALIDACIÓN DE TOKEN JWT
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // ... (el resto de tu lógica de validación de Claims se mantiene igual)
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtAuthtenticationConfig.getSecretKey())
                        .build()
                        .parseClaimsJws(authHeader.substring(7))
                        .getBody();

                String username = claims.getSubject();
                if (username != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, Collections.emptyList());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token invalido\", \"mensaje\": \"" + e.getMessage() + "\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Error: Token JWT ausente.");
        }
    }
}