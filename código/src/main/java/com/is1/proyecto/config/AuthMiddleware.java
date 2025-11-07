package com.is1.proyecto.config;

import spark.Filter;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import static spark.Spark.halt;

public class AuthMiddleware {
    public static Filter requireLogin = (request, response) -> {
        if (request.session().attribute("userId") == null) {
            if (request.headers("HX-Request") != null) {
                // Para peticiones HTMX, redirige con un header especial
                response.header("HX-Redirect", "/login");
                halt(401);
            } else {
                response.redirect("/login");
                halt();
            }
        }
    };

    public static Filter requireAdmin = (request, response) -> {
        requireLogin.handle(request, response);
        String role = request.session().attribute("role");
        if (!"ADMIN".equals(role)) {
            if (request.headers("HX-Request") != null) {
                response.header("HX-Redirect", "/error?message=Acceso no autorizado");
                halt(403);
            } else {
                response.redirect("/error?message=Acceso no autorizado");
                halt();
            }
        }
    };

    public static Filter requireProfesor = (request, response) -> {
        requireLogin.handle(request, response);
        String role = request.session().attribute("role");
        if (!"PROFESOR".equals(role) && !"ADMIN".equals(role)) {
            if (request.headers("HX-Request") != null) {
                response.header("HX-Redirect", "/error?message=Acceso no autorizado");
                halt(403);
            } else {
                response.redirect("/error?message=Acceso no autorizado");
                halt();
            }
        }
    };

    public static Filter requireEstudiante = (request, response) -> {
        requireLogin.handle(request, response);
        String role = request.session().attribute("role");
        if (!"ESTUDIANTE".equals(role)) {
            if (request.headers("HX-Request") != null) {
                response.header("HX-Redirect", "/error?message=Acceso no autorizado");
                halt(403);
            } else {
                response.redirect("/error?message=Acceso no autorizado");
                halt();
            }
        }
    };

    public static Map<String, Object> getBaseModel(spark.Request request) {
        Map<String, Object> model = new HashMap<>();
        String username = request.session().attribute("username");
        String role = request.session().attribute("role");
        
        if (username != null) {
            model.put("username", username);
            model.put("isAdmin", "ADMIN".equals(role));
            model.put("isProfesor", "PROFESOR".equals(role));
            model.put("isEstudiante", "ESTUDIANTE".equals(role));
        }
        
        return model;
    }
}