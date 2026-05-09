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
        String apiKeyHeader = request.getHeader("x-api-key");

        // 1. LISTA BLANCA (Whitelist)
        if (path.startsWith("/auth") || 
            path.contains("/vencidos") || 
            path.contains("/placa") || 
            path.contains("/operar") ||
            path.contains("/conteo")) {
            
            filterChain.doFilter(request, response);
            return;
        }

        // 2. VALIDACIÓN DE APIKEY (Para rutas privadas)
        if (apiKeyHeader == null || !usuarioRepo.existsByApikey(apiKeyHeader)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Error: APIKey invalida o ausente.");
            return;
        }

        // 3. VALIDACIÓN DE TOKEN JWT (ESTRICTA)
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // ESTO ES LO QUE VALIDA LA INTEGRIDAD:
                // Si borras una letra o el token es falso, parseClaimsJws lanzará una excepción.
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtAuthtenticationConfig.getSecretKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();

                if (username != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, Collections.emptyList());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Solo si el token es 100% válido llegamos aquí
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

                filterChain.doFilter(request, response);

            } catch (Exception e) {
                // Si el token está roto, expirado o manipulado, respondemos 403
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token invalido o manipulado\", \"mensaje\": \"" + e.getMessage() + "\"}");
            }
        } else {
            // No hay Bearer token
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Error: Token JWT ausente.");
        }
    }
}