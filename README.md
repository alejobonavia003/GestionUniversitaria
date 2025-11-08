# Sistema de Gestión Universitaria

Sistema web para la gestión de carreras, materias, profesores y estudiantes universitarios. Desarrollado con Java, Spark Framework y SQLite.

## Características

- Gestión de carreras y planes de estudio
- Administración de materias y correlatividades
- Registro de profesores y estudiantes
- Sistema de autenticación, autorización y roles
- Logs detallados por nivel (DEBUG, INFO, WARN, ERROR)
- Base de datos SQLite con modo WAL
- no tiene front-end solo back y una plantilla para el dashboard

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
java -jar target/proye-is-1.0-SNAPSHOT.jar
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
