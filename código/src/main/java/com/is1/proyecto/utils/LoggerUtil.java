package com.is1.proyecto.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilidad para el logging centralizado de la aplicación.
 */
public class LoggerUtil {
    private LoggerUtil() {} // Evitar instanciación

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}