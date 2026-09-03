package com.lumina.bff.config;

import com.lumina.bff.security.JwtRoleConverter;
import com.lumina.bff.security.MultiIssuerJwtDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MultiIssuerJwtDecoder jwtDecoder;
    private final JwtRoleConverter jwtRoleConverter;

    public SecurityConfig(MultiIssuerJwtDecoder jwtDecoder, JwtRoleConverter jwtRoleConverter) {
        this.jwtDecoder = jwtDecoder;
        this.jwtRoleConverter = jwtRoleConverter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**").permitAll()
                // login/registro de clientes: sin token todavia, es donde se consigue uno
                .requestMatchers("/api/usuarios/login", "/api/usuarios/registro").permitAll()
                // TODO (equipo): una vez que Front Publico/Front Cliente esten
                // definidos, revisar si el catalogo de productos debe ser
                // publico (GET) sin login. Por ahora todo exige token.
                //
                // Rutas de escritura de Productos/Bodega: solo ADMIN. Estas son
                // las que usa el Front Admin.
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers("/api/bodega/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder)
                    .jwtAuthenticationConverter(jwtRoleConverter)
                )
            );
        return http.build();
    }
}
