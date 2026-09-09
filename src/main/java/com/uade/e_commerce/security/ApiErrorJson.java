package com.uade.e_commerce.security;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

/**
 * El proyecto no trae Jackson en el classpath (spring-boot-starter-webmvc no lo incluye
 * por defecto en esta version de Spring Boot), y los componentes de seguridad se ejecutan
 * antes de que el HttpMessageConverter de Spring MVC entre en juego. Se arma el JSON a mano
 * para que la respuesta de error tenga la misma forma que ApiError/GlobalExceptionHandler.
 */
final class ApiErrorJson {

    private ApiErrorJson() {
    }

    static String build(HttpStatus status, String message, String path) {
        return "{"
                + "\"timestamp\":\"" + LocalDateTime.now() + "\","
                + "\"status\":" + status.value() + ","
                + "\"error\":\"" + escape(status.getReasonPhrase()) + "\","
                + "\"message\":\"" + escape(message) + "\","
                + "\"path\":\"" + escape(path) + "\","
                + "\"detalles\":[]"
                + "}";
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
