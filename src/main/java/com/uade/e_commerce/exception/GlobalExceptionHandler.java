package com.uade.e_commerce.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

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

    /**
     * Sin este handler, eliminar una categoria con productos asociados devolvia 500 en vez
     * del 409 que espera la coleccion de Postman.
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> manejarBusinessRule(BusinessRuleException ex, WebRequest request) {
        return construir(HttpStatus.CONFLICT, ex.getMessage(), request, List.of());
    }

    /**
     * Es la excepcion que lanza @Valid cuando el body no cumple las restricciones del DTO.
     * Se traduce a la misma forma de ApiError, con el detalle campo por campo.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + mensajeDe(error))
                .toList();

        return construir(HttpStatus.BAD_REQUEST, "Error de validacion en los datos enviados", request, detalles);
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
