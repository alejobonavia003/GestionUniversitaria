package com.is1.proyecto;

import static spark.Spark.*;
import org.javalite.activejdbc.Base;
import com.is1.proyecto.config.DBConfigSingleton;
import com.is1.proyecto.routes.AuthRoutes;
import com.is1.proyecto.routes.UserRoutes;
import com.is1.proyecto.routes.ProfesorRoutes;
import com.is1.proyecto.routes.GeneralRoutes;
import com.is1.proyecto.routes.CarreraRoutes;
import com.is1.proyecto.routes.PlanEstudioRoutes;
import com.is1.proyecto.routes.MateriaRoutes;
import org.slf4j.Logger;
import com.is1.proyecto.utils.LoggerUtil;
import java.io.File;

/**
 * Clase principal de la aplicación Spark.
 * Configura las rutas, filtros y el inicio del servidor web.
 */
public class App {
    private static final Logger logger = LoggerUtil.getLogger(App.class);

    public static void main(String[] args) {
        port(8080);

        // Asegurar directorio de logs antes del arranque
        String logDir = System.getProperty("LOG_DIR");
        if (logDir == null || logDir.isBlank()) {
            String envLogDir = System.getenv("LOG_DIR");
            logDir = (envLogDir != null && !envLogDir.isBlank()) ? envLogDir : "logs";
        }
        try {
            File dir = new File(logDir);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (created) {
                    System.out.println("Directorio de logs creado: " + dir.getAbsolutePath());
                } else {
                    System.out.println("No se pudo crear el directorio de logs: " + dir.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            System.err.println("Advertencia: fallo al asegurar directorio de logs: " + e.getMessage());
        }

        DBConfigSingleton dbConfig = DBConfigSingleton.getInstance();

        // 🔧 CONFIGURAR MODO WAL Y TIMEOUT UNA SOLA VEZ AL INICIO
        try {
            Base.open(dbConfig.getDriver(), dbConfig.getDbUrl() + "?busy_timeout=5000", dbConfig.getUser(), dbConfig.getPass());
            Base.exec("PRAGMA journal_mode = WAL;"); // activa el modo WAL
            logger.info("SQLite configurado en modo WAL con timeout de 5s ✅");
        } catch (Exception e) {
            logger.error("Error al configurar SQLite: {}", e.getMessage(), e);
        } finally {
            if (Base.hasConnection()) Base.close();
        }

        // 🧩 Manejamos conexión por request
        before((req, res) -> {
            try {
                if (!Base.hasConnection()) {
                    Base.open(dbConfig.getDriver(), dbConfig.getDbUrl() + "?busy_timeout=5000", dbConfig.getUser(), dbConfig.getPass());
                }
                logger.debug("Request URL: {}", req.url());
            } catch (Exception e) {
                logger.error("Error al abrir conexión con ActiveJDBC: {}", e.getMessage(), e);
                halt(500, "{\"error\": \"Error interno del servidor: Fallo al conectar a la base de datos.\"}");
            }
        });

        after((req, res) -> {
            try {
                if (Base.hasConnection()) {
                    Base.close();
                }
            } catch (Exception e) {
                logger.error("Error al cerrar conexión con ActiveJDBC: {}", e.getMessage(), e);
            }
        });

        // 🚀 Rutas de la app
        AuthRoutes.configure();
        UserRoutes.configure();
        ProfesorRoutes.configure();
        GeneralRoutes.configure();
        CarreraRoutes.configure();
        PlanEstudioRoutes.configure();
        MateriaRoutes.configure();
    }
}
