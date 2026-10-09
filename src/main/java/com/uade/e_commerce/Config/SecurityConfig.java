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

                .requestMatchers(HttpMethod.PUT, "/api/productos/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/productos/**")
                    .hasRole("ADMIN")

                // CATEGORÍAS

                .requestMatchers(HttpMethod.GET, "/api/categorias/**")
                    .permitAll()

                .requestMatchers(HttpMethod.POST, "/api/categorias/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.PUT, "/api/categorias/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/categorias/**")
                    .hasRole("ADMIN")

                // VARIANTES
                // No necesitan reglas propias: /api/productos/** ya las cubre con el mismo
                // criterio (GET publico, alta/modificacion/baja solo ADMIN).

                // CARRITO

                // El carrito es del comprador: el ADMIN administra, no compra.
                .requestMatchers("/api/carrito/**")
                    .hasRole("CLIENTE")

                // PEDIDOS

                // El checkout toma los items del carrito, asi que tambien es solo del CLIENTE.
                .requestMatchers(HttpMethod.POST, "/api/pedidos")
                    .hasRole("CLIENTE")

                // El cliente consulta el listado de sus pedidos mediante este endpoint.
                .requestMatchers(HttpMethod.GET, "/api/pedidos/mis-pedidos")
                    .hasRole("CLIENTE")

                // Detalle de un pedido. Va despues de /mis-pedidos porque el patron de un
                // segmento tambien lo matchearia. El CLIENTE entra aca, pero PedidoService
                // valida que el pedido sea suyo antes de devolverlo.
                .requestMatchers(HttpMethod.GET, "/api/pedidos/*")
                    .hasAnyRole("CLIENTE", "ADMIN")

                // Listado completo, pedidos de otro usuario, cambio de estado y eliminacion:
                // solo ADMIN.
                .requestMatchers("/api/pedidos/**")
                    .hasRole("ADMIN")

                // USUARIOS

                // Cada usuario autenticado puede consultar su propio perfil.
                .requestMatchers(HttpMethod.GET, "/usuarios/me")
                    .hasAnyRole("CLIENTE", "ADMIN")

                // Listado, consulta por ID, modificacion y eliminacion: solo ADMIN.
                .requestMatchers("/usuarios/**")
                    .hasRole("ADMIN")

                // RESTO:  Cualquier otro endpoint requiere autenticación
                .anyRequest().authenticated()
            )

            // Cabeceras de seguridad. Spring Security ya envia X-Content-Type-Options: nosniff
            // y X-Frame-Options: DENY por defecto; la CSP se declara para que un navegador no
            // ejecute nada si alguna respuesta llegara a interpretarse como HTML.
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'none'; frame-ancestors 'none'"))
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
