# Sistema de Logs

## Descripción General
Sistema simple de logging implementado con SLF4J + Logback que separa los logs por niveles en archivos diferentes.

## Estructura
```
logs/
├── debug.log    # Información detallada para desarrollo
├── info.log     # Operaciones normales del sistema
├── warn.log     # Advertencias y situaciones subóptimas
└── error.log    # Errores y excepciones
```

## Uso Básico

### 1. Agregar Logger a una Clase
```java
private static final Logger logger = LoggerUtil.getLogger(MiClase.class);
```

### 2. Niveles de Log
```java
// Debugging (desarrollo)
logger.debug("Detalles de variable: {}", valor);

// Información (operaciones normales)
logger.info("Operación completada: {}", operacion);

// Advertencias (situaciones no ideales)
logger.warn("Configuración subóptima: {}", config);

// Errores (problemas críticos)
logger.error("Error en operación", excepcion);
```

### 3. Cuándo Usar Cada Nivel

#### DEBUG
- Variables internas
- Flujo detallado
- Información de desarrollo
```java
logger.debug("Procesando usuario: {}", usuario.getId());
```

#### INFO
- Inicio/fin de operaciones
- Acciones importantes
- Estado del sistema
```java
logger.info("Usuario {} registrado exitosamente", usuario.getEmail());
```

#### WARN
- Configuraciones por defecto
- Reintentos
- Situaciones no críticas
```java
logger.warn("Usando configuración por defecto para {}", parametro);
```

#### ERROR
- Excepciones
- Fallos de operaciones
- Problemas críticos
```java
try {
    // operación
} catch (Exception e) {
    logger.error("Error al procesar pago: {}", e.getMessage(), e);
}
```

## Características
- Rotación diaria de archivos
- Retención por 30 días
- Stack traces completos en errores
- Timestamps en cada entrada

## Testing
Los tests están centralizados en `LoggingTest.java` y verifican:
- Separación correcta de niveles
- Creación de archivos
- Manejo de excepciones

## Mantenimiento
- Los archivos rotan automáticamente
- Se eliminan después de 30 días
- No requiere configuración adicional

## Configuración
Toda la configuración está en `src/main/resources/logback.xml`