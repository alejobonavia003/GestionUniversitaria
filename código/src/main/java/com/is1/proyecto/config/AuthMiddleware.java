package com.is1.proyecto.config;

import spark.Filter;
import spark.Request;
import spark.Response;
import static spark.Spark.halt;

import java.util.HashMap;
import java.util.Map;

/**
 * Contiene los filtros de SparkJava para manejar la autenticación y autorización
 * de las rutas, incluyendo el soporte para peticiones HTMX.
 */
public class AuthMiddleware {

    private static final String UNAUTHORIZED_MESSAGE = "Acceso no autorizado o insuficiente.";
    private static final String LOGIN_URL = "/login";
    private static final String ERROR_URL = "/error?message=" + UNAUTHORIZED_MESSAGE;

    /**
     * Muestra la vista de error o redirige, manejando peticiones normales y HTMX.
     * @param res Objeto de respuesta.
     * @param req Objeto de petición.
     * @param statusCode Código de estado HTTP (401 para no autenticado, 403 para no autorizado).
     * @param targetUrl URL a la que redirigir (ej: /login o /error).
     */
    private static void handleHalt(Request req, Response res, int statusCode, String targetUrl) {
        if (req.headers("HX-Request") != null) {
            // Manejo de HTMX: usa el header HX-Redirect para que el cliente redirija
            res.header("HX-Redirect", targetUrl);
            halt(statusCode);
        } else {
            // Manejo normal: redirección HTTP
            res.redirect(targetUrl);
            halt(); // Detiene el procesamiento de la solicitud
        }
    }

    // --- 1. Filtro Básico: Requerir Autenticación (Login) ---

    public static Filter requireLogin = (request, response) -> {
        // Usamos "loggedUserId" o "userId" como clave de sesión (asegúrate de que el AuthController use esta misma clave)
        if (request.session().attribute("loggedUserId") == null) { 
            handleHalt(request, response, 401, LOGIN_URL);
        }
        // Si el usuario está logueado, pasa al siguiente filtro o ruta
    };

    // --- 2. Filtros de Autorización (Roles) ---
    
    /**
     * Requisitos: Rol ADMIN.
     */
    public static Filter requireAdmin = (request, response) -> {
        // Se ejecuta requireLogin primero. Si el login falla, el handleHalt() lo detiene.
        requireLogin.handle(request, response); 
        
        String role = request.session().attribute("userRole");
        if (!"ADMIN".equals(role)) {
            handleHalt(request, response, 403, ERROR_URL);
        }
    };

    /**
     * Requisitos: Rol PROFESOR o ADMIN.
     */
    public static Filter requireProfesor = (request, response) -> {
        requireLogin.handle(request, response);
        
        String role = request.session().attribute("userRole");
        // Lógica de jerarquía: Si no es Profesor Y no es Admin, denegar.
        if (!"PROFESOR".equals(role) && !"ADMIN".equals(role)) {
            handleHalt(request, response, 403, ERROR_URL);
        }
    };

    /**
     * Requisitos: Rol ESTUDIANTE o ADMIN (asumiendo que el admin puede ver la info de estudiante).
     * NOTA: Aquí el filtro es un poco más restrictivo. Si quieres que solo el ESTUDIANTE
     * y el ADMIN puedan entrar, esta lógica es correcta.
     */
    public static Filter requireEstudiante = (request, response) -> {
        requireLogin.handle(request, response);
        
        String role = request.session().attribute("userRole");
        // Si no es Estudiante Y no es Admin, denegar.
        if (!"ESTUDIANTE".equals(role) && !"ADMIN".equals(role)) {
            handleHalt(request, response, 403, ERROR_URL);
        }
    };
    
    // El método getBaseModel es una utilidad excelente para las Vistas, 
    // pero idealmente debería vivir en una clase de utilidades de renderizado o el propio controlador.
    // Lo mantendremos aquí por ahora, pero sabes que su uso es para Vistas (no para seguridad).

     // --- 3. Utilidad para Vistas ---
    
    /**
     * Crea un Map (modelo) base para las plantillas Mustache,
     * cargando la información del usuario de la sesión.
     * @param request La petición Spark.
     * @return Un Map que contiene username, isAdmin, isProfesor, isEstudiante.
     */
    public static Map<String, Object> getBaseModel(Request request) {
        Map<String, Object> model = new HashMap<>();
        
        // Asegúrate de que la clave "username" coincida con la que guardas en AuthController
        String username = request.session().attribute("username"); 
        String role = request.session().attribute("userRole");
        
        if (username != null) {
            model.put("username", username);
            model.put("isLogged", true);
            model.put("isAdmin", "ADMIN".equals(role));
            model.put("isProfesor", "PROFESOR".equals(role));
            model.put("isEstudiante", "ESTUDIANTE".equals(role));
        } else {
            model.put("isLogged", false);
        }
        
        return model;
    }
}