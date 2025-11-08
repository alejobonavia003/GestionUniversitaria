package com.is1.proyecto;

import static spark.Spark.*;
import org.javalite.activejdbc.Base;
import com.is1.proyecto.config.DBConfigSingleton;
import com.is1.proyecto.routes.AuthRoutes;
import com.is1.proyecto.routes.CarreraRoutes;
import com.is1.proyecto.routes.UserRoutes;
import com.is1.proyecto.routes.GeneralRoutes;
import org.slf4j.Logger;
import com.is1.proyecto.utils.LoggerUtil;
import java.io.File;

/**
 * Clase principal de la aplicación Spark.
 * Configura las rutas, filtros y el inicio del servidor web.
 */
public class App {
    // Aseguramos que exista la carpeta logs antes de inicializar el logger
    static {
        try {
            File dir = new File("logs");
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (created) {
                    System.out.println("Carpeta logs creada correctamente");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al crear carpeta logs: " + e.getMessage());
        }
    }

    //incializamos el logger
    private static final Logger logger = LoggerUtil.getLogger(App.class);

    public static void main(String[] args) {

        port(8080);

        DBConfigSingleton dbConfig = DBConfigSingleton.getInstance();
        logger.debug("Configuración de base de datos: {}", dbConfig.getDbUrl());

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

        // Rutas de la app
        AuthRoutes.configure();
        UserRoutes.configure();
        CarreraRoutes.configure();
        GeneralRoutes.configure();
    }
}
