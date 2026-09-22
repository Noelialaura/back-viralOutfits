package com.uade.e_commerce.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.uade.e_commerce.security.JwtAuthenticationEntryPoint;
import com.uade.e_commerce.security.JwtAuthenticationFilter;
import com.uade.e_commerce.security.RestAccessDeniedHandler;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          RestAccessDeniedHandler restAccessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
            // La API no usa sesiones: cada request se autentica con el token del header.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
    
                // PÚBLICOS
                .requestMatchers("/auth/registro", "/auth/login")
                    .permitAll()

                // PRODUCTOS

                .requestMatchers(HttpMethod.GET, "/api/productos/**")
                    .permitAll()

                .requestMatchers(HttpMethod.POST, "/api/productos/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/productos/**")
                    .hasRole("ADMIN")

                // CATEGORÍAS

                .requestMatchers(HttpMethod.GET, "/api/categorias/**")
                    .permitAll()

                .requestMatchers(HttpMethod.POST, "/api/categorias/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/categorias/**")
                    .hasRole("ADMIN")

                // CARRITO

                .requestMatchers("/api/carrito/**")
                    .hasRole("CLIENTE")


                // USUARIOS

                .requestMatchers(HttpMethod.GET, "/usuarios/**")
                    .hasAnyRole("CLIENTE", "ADMIN")

                // RESTO:  Cualquier otro endpoint requiere autenticación
                .anyRequest().authenticated()
            )

            // Manejo de errores de seguridad
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)
            )

            // Filtro JWT antes del filtro estándar de autenticación
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            )

            .build();
    }
}
