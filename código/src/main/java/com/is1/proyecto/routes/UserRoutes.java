package com.is1.proyecto.routes;

import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import org.mindrot.jbcrypt.BCrypt;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.is1.proyecto.models.User;
import com.is1.proyecto.config.DBConfigSingleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRoutes {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void configure() {
        get("/user/create", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            String successMessage = req.queryParams("message");
            if (successMessage != null && !successMessage.isEmpty()) {
                model.put("successMessage", successMessage);
            }

            String errorMessage = req.queryParams("error");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                model.put("errorMessage", errorMessage);
            }

            return new ModelAndView(model, "user_form.mustache");
        }, new MustacheTemplateEngine());

        get("/user/new", (req, res) -> {
            return new ModelAndView(new HashMap<>(), "user_form.mustache");
        }, new MustacheTemplateEngine());

        post("/user/new", (req, res) -> {
            String name = req.queryParams("name");
            String password = req.queryParams("password");

            String role = req.queryParams("role");

            if (role == null || role.isEmpty()) {
                // por defecto registrar como estudiante/alumno
                role = "ESTUDIANTE";
            }

            // Normalizar y validar rol
            role = role.toUpperCase();
            if (!role.equals("ADMIN") && !role.equals("PROFESOR") && !role.equals("ESTUDIANTE") && !role.equals("USER")) {
                // valores inválidos
                res.status(400);
                res.redirect("/user/create?error=Rol inválido.");
                return "";
            }

            if (name == null || name.isEmpty() || password == null || password.isEmpty()) {
                res.status(400);
                res.redirect("/user/create?error=Nombre y contraseña son requeridos.");
                return "";
            }

            try {
                // Ensure the DB has the 'role' column (runtime migration for older DBs)
                DBConfigSingleton.getInstance().ensureUserRoleColumn();

                User ac = new User();
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                ac.set("name", name);
                ac.set("password", hashedPassword);
                ac.saveIt();

                // Try to set role via ActiveJDBC; if the model metadata does not contain 'role',
                // fallback to a direct JDBC UPDATE.
                try {
                    ac.set("role", role);
                    ac.saveIt();
                } catch (IllegalArgumentException iae) {
                    try {
                        String url = DBConfigSingleton.getInstance().getDbUrl();
                        String urlWithTimeout = url.contains("?") ? url + "&busy_timeout=5000" : url + "?busy_timeout=5000";
                        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(urlWithTimeout)) {
                            try (java.sql.PreparedStatement ps = conn.prepareStatement("UPDATE users SET role = ? WHERE id = ?")) {
                                ps.setString(1, role);
                                ps.setObject(2, ac.getId());
                                ps.executeUpdate();
                            }
                        }
                    } catch (Exception ex) {
                        System.err.println("Fallback update role failed: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }

                res.status(201);
                res.redirect("/user/create?message=Cuenta creada exitosamente para " + name + "!");
                return "";

            } catch (Exception e) {
                System.err.println("Error al registrar la cuenta: " + e.getMessage());
                e.printStackTrace();
                res.status(500);
                res.redirect("/user/create?error=Error interno al crear la cuenta. Intente de nuevo.");
                return "";
            }
        });

        post("/add_users", (req, res) -> {
            res.type("application/json");

            String name = req.queryParams("name");
            String password = req.queryParams("password");

            if (name == null || name.isEmpty() || password == null || password.isEmpty()) {
                res.status(400);
                return objectMapper.writeValueAsString(Map.of("error", "Nombre y contraseña son requeridos."));
            }

            try {
                // Ensure the DB has the 'role' column before creating via API
                DBConfigSingleton.getInstance().ensureUserRoleColumn();

                String role = req.queryParams("role");
                if (role == null || role.isEmpty()) {
                    role = "ESTUDIANTE";
                }
                role = role.toUpperCase();
                if (!role.equals("ADMIN") && !role.equals("PROFESOR") && !role.equals("ESTUDIANTE") && !role.equals("USER")) {
                    res.status(400);
                    return objectMapper.writeValueAsString(Map.of("error", "Rol inválido."));
                }

                User newUser = new User();
                newUser.set("name", name);
                String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
                newUser.set("password", hashed);
                newUser.saveIt();

                try {
                    newUser.set("role", role);
                    newUser.saveIt();
                } catch (IllegalArgumentException iae) {
                    try {
                        String url = DBConfigSingleton.getInstance().getDbUrl();
                        String urlWithTimeout = url.contains("?") ? url + "&busy_timeout=5000" : url + "?busy_timeout=5000";
                        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(urlWithTimeout)) {
                            try (java.sql.PreparedStatement ps = conn.prepareStatement("UPDATE users SET role = ? WHERE id = ?")) {
                                ps.setString(1, role);
                                ps.setObject(2, newUser.getId());
                                ps.executeUpdate();
                            }
                        }
                    } catch (Exception ex) {
                        System.err.println("Fallback update role failed (API): " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }

                res.status(201);
                return objectMapper.writeValueAsString(Map.of("message", "Usuario '" + name + "' registrado con éxito.", "id", newUser.getId()));

            } catch (Exception e) {
                System.err.println("Error al registrar usuario: " + e.getMessage());
                e.printStackTrace();
                res.status(500);
                return objectMapper.writeValueAsString(Map.of("error", "Error interno al registrar usuario: " + e.getMessage()));
            }
        });
    }
}