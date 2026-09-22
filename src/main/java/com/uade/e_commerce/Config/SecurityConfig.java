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
            // La API no usa sesiones: cada request se autentica con el token del header.
        return http
            // 1. Deshabilitar CSRF (innecesario para APIs REST sin cookies de sesión)
            .csrf(csrf -> csrf.disable())

            // 2. Sesiones sin estado (Stateless): la autenticación depende únicamente del token JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 3. Reglas de autorización de endpoints
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas: registro, login y el dispatch interno de errores
                .requestMatchers("/auth/**", "/error").permitAll()

                // Consulta pública del catálogo de productos y categorías
                .requestMatchers(HttpMethod.GET, "/api/productos/**", "/api/categorias/**").permitAll()

                // Gestión de catálogo restringida a rol ADMIN
                .requestMatchers(HttpMethod.POST, "/api/productos/**", "/api/categorias/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**", "/api/categorias/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/productos/**", "/api/categorias/**").hasRole("ADMIN")

                // Operaciones de clientes que exigen token válido
                .requestMatchers("/api/carrito/**").authenticated()
                .requestMatchers("/api/pedidos/**").authenticated()
                .requestMatchers("/usuarios/me").authenticated()

                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
            )

            // 4. Manejo de excepciones de seguridad (401 Unauthorized y 403 Forbidden en formato JSON)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)
            )

            // 5. Inserción del filtro JWT antes del filtro estándar de usuario y contraseña
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

            .build();
    }
}