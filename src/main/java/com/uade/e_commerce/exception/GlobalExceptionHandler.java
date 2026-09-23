package com.uade.e_commerce.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> manejarResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), request, List.of());
    }

    @ExceptionHandler(ArgumentInvalidException.class)
    public ResponseEntity<ApiError> manejarArgumentInvalid(ArgumentInvalidException ex, WebRequest request) {
        return construir(HttpStatus.BAD_REQUEST, ex.getMessage(), request, List.of());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> manejarInvalidCredentials(InvalidCredentialsException ex, WebRequest request) {
        return construir(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, List.of());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> manejarBusinessRule(BusinessRuleException ex, WebRequest request) {
        return construir(HttpStatus.CONFLICT, ex.getMessage(), request, List.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> manejarIntegridad(DataIntegrityViolationException ex, WebRequest request) {
        return construir(HttpStatus.CONFLICT,
                "La operacion viola una restriccion de integridad de los datos", request, List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + mensajeDe(error))
                .toList();

        return construir(HttpStatus.BAD_REQUEST, "Error de validacion en los datos enviados", request, detalles);
    }

    // Los 403 que lanzan los services (por ejemplo, consultar un pedido ajeno) salen con el
    // mismo formato ApiError que el resto de los errores de la API.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> manejarAccesoDenegado(AccessDeniedException ex, WebRequest request) {
        return construir(HttpStatus.FORBIDDEN, ex.getMessage(), request, List.of());
    }

    // JSON mal formado o con un valor que no entra en el tipo destino (por ejemplo, un
    // estadoPedido que no existe en el enum).
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> manejarJsonInvalido(HttpMessageNotReadableException ex, WebRequest request) {
        return construir(HttpStatus.BAD_REQUEST,
                "El cuerpo de la solicitud no es un JSON valido o tiene un valor no permitido",
                request, List.of());
    }

    // Parametro de URL con el tipo equivocado, por ejemplo /api/productos/abc.
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> manejarTipoInvalido(MethodArgumentTypeMismatchException ex, WebRequest request) {
        return construir(HttpStatus.BAD_REQUEST,
                "El parametro '" + ex.getName() + "' tiene un valor invalido", request, List.of());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> manejarRutaInexistente(NoResourceFoundException ex, WebRequest request) {
        return construir(HttpStatus.NOT_FOUND, "La ruta solicitada no existe", request, List.of());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> manejarMetodoNoSoportado(HttpRequestMethodNotSupportedException ex,
            WebRequest request) {
        return construir(HttpStatus.METHOD_NOT_ALLOWED,
                "El metodo " + ex.getMethod() + " no esta permitido en esta ruta", request, List.of());
    }

    // Red de seguridad, siempre ultimo: sin esto cualquier excepcion no contemplada sale como el
    // 500 crudo de Spring en vez de respetar el formato ApiError del resto de la API.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarErrorInesperado(Exception ex, WebRequest request) {
        return construir(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrio un error inesperado al procesar la solicitud", request, List.of());
    }

    private String mensajeDe(FieldError error) {
        return error.getDefaultMessage() == null ? "valor invalido" : error.getDefaultMessage();
    }

    private ResponseEntity<ApiError> construir(HttpStatus status, String mensaje, WebRequest request,
            List<String> detalles) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                request.getDescription(false).replace("uri=", ""),
                detalles);

        return ResponseEntity.status(status).body(error);
    }
}
