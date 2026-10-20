
package com.ragplatform.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
            // Enable CORS for the React frontend
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Disable CSRF for the stateless REST API
            .csrf(csrf -> csrf.disable())

            // JWT-based authentication; no server sessions
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // Public endpoints
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login"
                ).permitAll()

                // All other endpoints require authentication
                .anyRequest().authenticated()
            )

            // Validate Bearer JWTs
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> {})
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // Local React development server
        configuration.setAllowedOrigins(
            List.of("http://localhost:5173")
        );

        // Add the deployed React URL through an environment variable
        String frontendUrl = System.getenv("FRONTEND_URL");

        if (frontendUrl != null && !frontendUrl.isBlank()) {
            configuration.setAllowedOrigins(
                List.of("http://localhost:5173", frontendUrl)
            );
        }

        configuration.setAllowedMethods(
            List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
            )
        );

        configuration.setAllowedHeaders(
            List.of(
                "Authorization",
                "Content-Type",
                "Accept"
            )
        );

        // Bearer-token authentication does not require cookies
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}