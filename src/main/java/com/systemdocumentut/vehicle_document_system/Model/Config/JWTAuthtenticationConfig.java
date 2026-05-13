package com.systemdocumentut.vehicle_document_system.Model.Config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Configuración para la generación de tokens JWT.
 * Requerimiento: Los servicios deben estar configurados de forma segura mediante token.
 */
@Configuration
public class JWTAuthtenticationConfig {

    /**
     * Genera una llave secreta robusta de 512 bits.
     * Esto previene el error de "llave demasiado corta" en implementaciones JWT modernas.
     */
    private final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    /**
     * Expone la llave secreta para que el JwtFilter pueda validar los tokens entrantes.
     */
    public SecretKey getSecretKey() {
        return this.KEY;
    }

    /**
     * Genera un token JWT para un usuario autenticado.
     * * @param username El login del usuario (basado en la nemotecnia del requerimiento).
     * @return El token JWT con el prefijo "Bearer ".
     */
    public String getJWTToken(String username) {
        // Definimos el rol, por defecto ADMIN según los requerimientos de acceso administrativo
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_ADMIN");

        String token = Jwts.builder()
                .setId("softutJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Tiempo de expiración configurado (aprox. 20 días en este ejemplo)
                .setExpiration(new Date(System.currentTimeMillis() + 1800000000))
                .signWith(KEY)
                .compact();

        return "Bearer " + token;
    }
}