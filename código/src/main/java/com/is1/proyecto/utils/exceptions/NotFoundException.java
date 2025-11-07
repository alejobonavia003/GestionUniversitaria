package com.is1.proyecto.utils.exceptions;

/**
 * Excepción para entidades no encontradas.
 */
public class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(message);
    }
}