// Archivo: com/is1/proyecto/config/DBConfigSingleton.java
package com.is1.proyecto.config;

import org.javalite.activejdbc.Base; // Necesitarás esta importación para usar Base.open y Base.close
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public final class DBConfigSingleton {

    private static DBConfigSingleton instance;

    // Ya no es necesario que sean final si los vas a configurar dinámicamente o mantener una sola instancia
    private final String dbUrl;
    private final String user;
    private final String pass;
    private final String driver;

    // Constructor privado para evitar instanciación directa
    private DBConfigSingleton() {
        // Configuraciones para SQLite
        this.driver = "org.sqlite.JDBC"; // Driver JDBC para SQLite
        this.dbUrl = System.getProperty("db.url", "jdbc:sqlite:./db/dev.db");
        this.user = ""; // SQLite no usa usuario
        this.pass = ""; // SQLite no usa contraseña
    }

    public static synchronized DBConfigSingleton getInstance() {
        if (instance == null) {
            instance = new DBConfigSingleton();
        }
        return instance;
    }

    // Métodos para abrir y cerrar la conexión
    public void openConnection() {
        // Abrir conexión sólo si no existe una en el hilo actual
        if (!Base.hasConnection()) {
            Base.open(this.driver, this.dbUrl, this.user, this.pass);
        }
    }

    public void closeConnection() {
        if (Base.hasConnection()) {
            Base.close();
        }
    }

    /**
     * Ensure that the `role` column exists in the `users` table. If it does not,
     * attempt to add it (SQLite supports ALTER TABLE ADD COLUMN).
     * This method is safe to call multiple times.
     */
    public void ensureUserRoleColumn() {
        String url = this.dbUrl;
        String urlWithTimeout = url.contains("?") ? url + "&busy_timeout=5000" : url + "?busy_timeout=5000";

        // First, check if the column exists using a fresh JDBC connection
        try (Connection conn = DriverManager.getConnection(urlWithTimeout)) {
            // Use PRAGMA table_info for SQLite - reliable
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("PRAGMA table_info('users')")) {
                while (rs.next()) {
                    String colName = rs.getString("name");
                    if (colName != null && "role".equalsIgnoreCase(colName)) {
                        return; // column exists
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking 'role' column existence: " + e.getMessage());
            // proceed to try altering (the error might be transient)
        }

        // Column not found — attempt to add it, with retries in case the DB is locked.
        int attempts = 0;
        final int maxAttempts = 5;
        while (attempts < maxAttempts) {
            try (Connection conn = DriverManager.getConnection(urlWithTimeout)) {
                try (Statement alter = conn.createStatement()) {
                    alter.executeUpdate("ALTER TABLE users ADD COLUMN role TEXT NOT NULL DEFAULT 'USER'");
                    System.out.println("DB migration: added 'role' column to users table.");
                    return;
                }
            } catch (Exception e) {
                String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
                if (msg.contains("database is locked") || msg.contains("busy")) {
                    attempts++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    continue; // retry
                }
                System.err.println("Error ensuring 'role' column exists: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }

    // Getters existentes
    public String getDbUrl() {
        return dbUrl;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getDriver() {
        return driver;
    }
}

