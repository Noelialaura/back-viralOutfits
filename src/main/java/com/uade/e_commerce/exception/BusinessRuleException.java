package com.uade.e_commerce.exception;

/**
 * Se lanza cuando una operacion es valida sintacticamente pero viola una regla de negocio
 * (por ejemplo, eliminar una categoria que todavia tiene productos asociados).
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
