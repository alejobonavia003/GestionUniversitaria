package com.is1.proyecto.utils.exceptions;

/**
 * Excepción base para todas las excepciones de negocio de la aplicación.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}