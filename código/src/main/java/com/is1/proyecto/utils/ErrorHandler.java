package com.is1.proyecto.utils;

import com.is1.proyecto.utils.exceptions.BusinessException;
import com.is1.proyecto.utils.exceptions.NotFoundException;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de errores para la aplicación.
 * nos ayuda a centralizar el manejo de excepciones y
 * retornar respuestas adecuadas según el tipo de error
 */
public class ErrorHandler {
    
    public static Object handleException(Exception e, Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        
        if (e instanceof NotFoundException) {
            res.status(404);
            model.put("error", "Recurso no encontrado: " + e.getMessage());
        } else if (e instanceof BusinessException) {
            res.status(400);
            model.put("error", "Error de negocio: " + e.getMessage());
        } else {
            res.status(500);
            model.put("error", "Error interno del servidor: " + e.getMessage());
        }
        
        // Si la petición espera JSON
        String accept = req.headers("Accept");
        if (accept != null && accept.contains("application/json")) {
            res.type("application/json");
            return JsonUtil.toJson(model);
        }
        
        // Por defecto, retornar vista de error
        return new spark.template.mustache.MustacheTemplateEngine()
            .render(new spark.ModelAndView(model, "error.mustache"));
    }
}