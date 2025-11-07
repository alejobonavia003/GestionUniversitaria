# Sistema de Gestión Universitaria

Sistema web para la gestión de carreras, materias, profesores y estudiantes universitarios. Desarrollado con Java, Spark Framework y SQLite.

## Características

- Gestión de carreras y planes de estudio
- Administración de materias y correlatividades
- Registro de profesores y estudiantes
- Sistema de autenticación y autorización
- Logs detallados por nivel (DEBUG, INFO, WARN, ERROR)
- Base de datos SQLite con modo WAL

## Requisitos Previos

- Java 11 o superior
- Maven 3.6 o superior
- SQLite 3

## Configuración Inicial

1. Clonar el repositorio:
```bash
git clone https://github.com/alejobonavia003/GestionUniversitaria.git
cd GestionUniversitaria/código
```

2. Crear la base de datos:
```bash
mkdir -p db
sqlite3 db/dev.db < src/main/resources/scheme.sql
```

3. Compilar el proyecto:
```bash
mvn clean package
```

## Ejecución

Para ejecutar la aplicación:

```bash
java --enable-native-access=ALL-UNNAMED -jar target/proye-is-1.0-SNAPSHOT.jar
```

La aplicación estará disponible en: http://localhost:8080

## Estructura del Proyecto

```
código/
├── src/
│   ├── main/
│   │   ├── java/com/is1/proyecto/
│   │   │   ├── config/        # Configuración de la aplicación
│   │   │   ├── controllers/   # Controladores MVC
│   │   │   ├── models/        # Modelos de datos
│   │   │   ├── repositories/  # Acceso a datos
│   │   │   ├── routes/        # Rutas de la aplicación
│   │   │   └── utils/         # Utilidades
│   │   └── resources/
│   │       ├── templates/     # Plantillas Mustache
│   │       └── logback.xml    # Configuración de logs
│   └── test/                  # Tests unitarios
├── db/                        # Base de datos SQLite
└── logs/                      # Archivos de log
```

## Documentación

- [Sistema de Logs](documentación/sistema_logs.md)
- [API Rest](documentación/api.md)
- [Base de Datos](documentación/base_datos.md)

## Desarrollo

### Logs
```java
private static final Logger logger = LoggerUtil.getLogger(MiClase.class);

logger.debug("Detalles técnicos");      // En logs/debug.log
logger.info("Operación normal");         // En logs/info.log
logger.warn("Advertencia");              // En logs/warn.log
logger.error("Error crítico", error);    // En logs/error.log
```

### Tests
Ejecutar los tests:
```bash
mvn test
```

## Contribuir

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add: alguna característica'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.
