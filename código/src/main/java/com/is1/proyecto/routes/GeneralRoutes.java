package com.is1.proyecto.routes;

import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import com.is1.proyecto.config.AuthMiddleware; // ¡Importamos el Middleware!

/**
 * Define las rutas generales de la aplicación (Dashboard, Perfil, Error).
 * Protege las rutas que requieren inicio de sesión.
 */
public class GeneralRoutes {
    public static void configure() {
        
        MustacheTemplateEngine engine = new MustacheTemplateEngine();

        // --- APLICACIÓN DEL MIDDLEWARE ---
        // Aplicamos el filtro requireLogin (el más básico) a las rutas
        // que lo necesitan, como /dashboard y /profile.
        // El filtro manual que tenías antes se elimina para usar este.
        before("/dashboard", AuthMiddleware.requireLogin);
        before("/profile", AuthMiddleware.requireLogin);
        
        // --- RUTAS PÚBLICAS (O semi-públicas) ---

        // La ruta de error debe ser pública para que AuthMiddleware pueda redirigir a ella.
        get("/error", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            // El AuthMiddleware redirige a /error?message=...
            model.put("errorMessage", req.queryParams("message")); 
            
            // Agregar información de usuario si está logueado (para el layout)
            String username = req.session().attribute("name");
            String role = req.session().attribute("userRole");
            if (username != null) {
                model.put("username", username);
                model.put("isAdmin", "ADMIN".equals(role));
                model.put("isProfesor", "PROFESOR".equals(role));
                model.put("isEstudiante", "ESTUDIANTE".equals(role));
            }
            return new ModelAndView(model, "error.mustache");
        }, engine);


        // --- RUTAS PRIVADAS (Protegidas por el Middleware) ---

        get("/dashboard", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            
            // Ya no necesitamos el check de "username == null"
            // porque el AuthMiddleware.requireLogin ya lo hizo.
            String username = req.session().attribute("name");
            System.out.println("DEBUGG:::: username:" + username);
            String role = req.session().attribute("userRole");
            System.out.println("DEBUGG:::: role:" + role);

            model.put("username", username);
            model.put("isAdmin", "ADMIN".equals(role));
            model.put("isProfesor", "PROFESOR".equals(role));

            model.put("isEstudiante", "ESTUDIANTE".equals(role));

            
            
            return new ModelAndView(model, "dashboard.mustache");
        }, engine);

        get("/profile", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            
            // Ya no necesitamos el check de "username == null"
            String username = req.session().attribute("username");
            String role = req.session().attribute("userRole");
            
            model.put("username", username);
            model.put("isAdmin", "ADMIN".equals(role));
            model.put("isProfesor", "PROFESOR".equals(role));
            model.put("isEstudiante", "ESTUDIANTE".equals(role));
            
            
            return new ModelAndView(model, "profile.mustache");
        }, engine);

        // La ruta de /logout se eliminó de aquí.
        // Ya está definida (y es su lugar correcto) en AuthRoutes.java.
    }
}