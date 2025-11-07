package com.is1.proyecto.routes;

import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import org.mindrot.jbcrypt.BCrypt;
import com.is1.proyecto.models.User;
import com.is1.proyecto.config.DBConfigSingleton;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

public class AuthRoutes {
    public static void configure() {
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String errorMessage = req.queryParams("error");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                model.put("errorMessage", errorMessage);
            }
            String successMessage = req.queryParams("message");
            if (successMessage != null && !successMessage.isEmpty()) {
                model.put("successMessage", successMessage);
            }
            return new ModelAndView(model, "login.mustache");
        }, new MustacheTemplateEngine());

        // Alias para /login (muchas requests usan /login)
        get("/login", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String errorMessage = req.queryParams("error");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                model.put("errorMessage", errorMessage);
            }
            String successMessage = req.queryParams("message");
            if (successMessage != null && !successMessage.isEmpty()) {
                model.put("successMessage", successMessage);
            }
            return new ModelAndView(model, "login.mustache");
        }, new MustacheTemplateEngine());

        get("/logout", (req, res) -> {
            req.session().invalidate();
            res.redirect("/");
            return null;
        });

        post("/login", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            String username = req.queryParams("username");
            String plainTextPassword = req.queryParams("password");

            if (username == null || username.isEmpty() || plainTextPassword == null || plainTextPassword.isEmpty()) {
                res.status(400);
                model.put("errorMessage", "El nombre de usuario y la contraseña son requeridos.");
                return new ModelAndView(model, "login.mustache");
            }

            User ac = User.findFirst("name = ?", username);
            if (ac == null) {
                res.status(401);
                model.put("errorMessage", "Usuario o contraseña incorrectos.");
                return new ModelAndView(model, "login.mustache");
            }

            String storedHashedPassword = ac.getString("password");
            if (storedHashedPassword == null) {
                res.status(500);
                model.put("errorMessage", "Cuenta inválida: falta contraseña.");
                return new ModelAndView(model, "login.mustache");
            }

            if (!BCrypt.checkpw(plainTextPassword, storedHashedPassword)) {
                res.status(401);
                model.put("errorMessage", "Usuario o contraseña incorrectos.");
                return new ModelAndView(model, "login.mustache");
            }

            // Autenticación exitosa — establecer sesión
            req.session(true);
            req.session().attribute("userId", ac.getId());
            req.session().attribute("username", ac.getString("name"));
            String role = null;
            try {
                role = ac.getString("role");
            } catch (IllegalArgumentException iae) {
                // Fallback: query role directly via JDBC in case the ActiveJDBC meta-model
                // doesn't yet know about the column (runtime migration scenarios).
                try {
                    String url = DBConfigSingleton.getInstance().getDbUrl();
                    String urlWithTimeout = url.contains("?") ? url + "&busy_timeout=5000" : url + "?busy_timeout=5000";
                    try (Connection conn = DriverManager.getConnection(urlWithTimeout)) {
                        try (PreparedStatement ps = conn.prepareStatement("SELECT role FROM users WHERE id = ?")) {
                            ps.setObject(1, ac.getId());
                            try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                    role = rs.getString("role");
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Failed to read role via JDBC fallback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

            req.session().attribute("role", role != null ? role : "USER");

            // Redirigir al dashboard
            res.redirect("/dashboard");
            return null;
        }, new MustacheTemplateEngine());
    }
}