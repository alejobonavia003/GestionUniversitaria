package com.is1.proyecto.routes;

import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class GeneralRoutes {
    public static void configure() {
        before((request, response) -> {
            // Verificar si la ruta requiere autenticación
            String path = request.pathInfo();
            if (!path.equals("/login") && !path.equals("/user/new") && !path.contains("assets")) {
                if (request.session(false) == null || request.session().attribute("userId") == null) {
                    response.redirect("/login");
                    halt();
                }
            }
        });//

        get("/dashboard", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            
            String username = req.session().attribute("username");
            String role = req.session().attribute("role");
            
            if (username == null) {
                res.redirect("/login?error=Debes iniciar sesión para acceder a esta página.");
                return null;
            }

            model.put("username", username);
            model.put("isAdmin", "ADMIN".equals(role));
            model.put("isProfesor", "PROFESOR".equals(role));
            model.put("isEstudiante", "ESTUDIANTE".equals(role));
            
            // Agregar breadcrumbs
            Map<String, String> breadcrumb = new HashMap<>();
            breadcrumb.put("text", "Dashboard");
            breadcrumb.put("active", "true");
            model.put("breadcrumbs", new Map[]{breadcrumb});
            
            return new ModelAndView(model, "dashboard.mustache");
        }, new MustacheTemplateEngine());

        get("/error", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String errorMessage = req.queryParams("error");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                model.put("errorMessage", errorMessage);
            }
            
            // Agregar información de usuario si está logueado
            String username = req.session().attribute("username");
            String role = req.session().attribute("role");
            if (username != null) {
                model.put("username", username);
                model.put("isAdmin", "ADMIN".equals(role));
                model.put("isProfesor", "PROFESOR".equals(role));
                model.put("isEstudiante", "ESTUDIANTE".equals(role));
            }
            
            return new ModelAndView(model, "error.mustache");
        }, new MustacheTemplateEngine());

        // Ruta para el perfil
        get("/profile", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String username = req.session().attribute("username");
            String role = req.session().attribute("role");
            
            if (username == null) {
                res.redirect("/login");
                return null;
            }
            
            model.put("username", username);
            model.put("isAdmin", "ADMIN".equals(role));
            model.put("isProfesor", "PROFESOR".equals(role));
            model.put("isEstudiante", "ESTUDIANTE".equals(role));
            
            // Agregar breadcrumbs
            Map<String, String> breadcrumb = new HashMap<>();
            breadcrumb.put("text", "Mi Perfil");
            breadcrumb.put("active", "true");
            model.put("breadcrumbs", new Map[]{breadcrumb});
            
            return new ModelAndView(model, "profile.mustache");
        }, new MustacheTemplateEngine());

        // Ruta de cierre de sesión
        get("/logout", (req, res) -> {
            req.session().invalidate();
            res.redirect("/login");
            return null;
        });
    }
}