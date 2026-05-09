package com.systemdocumentut.vehicle_document_system.Model.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 1. Rutas de Autenticación (Login)
                .requestMatchers("/auth/**").permitAll()
                
                // 2. Servicios PÚBLICOS (Requerimiento Entrega 2, pág 4)
                // No requieren Token ni APIKey
                .requestMatchers(HttpMethod.GET, "/api/vehiculos/vencidos").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/conductores/operar").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/vehiculos/placa/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/personas/conteo").permitAll()
                
                // 3. Todo lo demás requiere TOKEN + APIKEY
                .anyRequest().authenticated()
            );

        // Agregamos el filtro JWT antes del filtro de usuario/contraseña
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Usamos NoOp porque las contraseñas en DB están en texto plano según laboratorios previos
        return NoOpPasswordEncoder.getInstance();
    }
}