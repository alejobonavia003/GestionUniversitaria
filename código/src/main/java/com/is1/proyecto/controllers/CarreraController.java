package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Carrera;
import com.is1.proyecto.services.CarreraService;
import com.is1.proyecto.utils.LoggerUtil;
import org.slf4j.Logger;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador para gestionar las peticiones web (HTTP) relacionadas con la entidad Carrera.
 * Actúa como intermediario entre las Rutas (URLs) y el Servicio (Lógica de Negocio).
 */
public class CarreraController {

    private static final Logger logger = LoggerUtil.getLogger(CarreraController.class);
    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    /**
     * [GET /admin/carreras]
     * Muestra la lista de todas las carreras.
     */
    public ModelAndView listCarreras(Request req, Response res) {
        logger.info("Solicitud GET /admin/carreras recibida.");
        Map<String, Object> model = new HashMap<>();
        try {
            List<Carrera> carreras = carreraService.getAllCarreras();
            model.put("carreras", carreras);
            // Agregamos mensajes de éxito si venimos de una redirección
            model.put("successMessage", req.queryParams("success"));
        } catch (Exception e) {
            logger.error("Error al obtener la lista de carreras.", e);
            model.put("errorMessage", "Error al cargar las carreras: " + e.getMessage());
        }
        return new ModelAndView(model, "admin/carreras/index.mustache");
    }

    /**
     * [GET /admin/carreras/nueva]
     * Muestra el formulario para crear una nueva carrera.
     */
    public ModelAndView showCreateForm(Request req, Response res) {
        logger.debug("Solicitud GET /admin/carreras/nueva");
        Map<String, Object> model = new HashMap<>();
        
        //TODO: ta raro esto
        // Pasamos valores previos si hubo un error en un intento anterior
        model.put("codCarrera", req.queryParams("codCarrera"));
        model.put("nombre", req.queryParams("nombre"));
        model.put("duracion", req.queryParams("duracion"));
        model.put("errorMessage", req.queryParams("error"));
        return new ModelAndView(model, "admin/carreras/formulario.mustache");
    }

    /**
     * [POST /admin/carreras/nueva]
     * Procesa los datos del formulario para crear una nueva carrera.
     */
    public Object createCarrera(Request req, Response res) {
        // Obtenemos los datos del formulario (query parameters)
        String codStr = req.queryParams("codCarrera");
        String nombre = req.queryParams("nombre");
        String duracionStr = req.queryParams("duracion");

        try {
            // Conversión y validación básica
            int codCarrera = Integer.parseInt(codStr);
            int duracion = Integer.parseInt(duracionStr);

            // Llamamos al servicio para aplicar la lógica de negocio
            carreraService.createCarrera(codCarrera, nombre, duracion);
            
            // Si tiene éxito, redirige al listado con un mensaje
            res.redirect("/admin/carreras?success=Carrera creada exitosamente.");
            
        } catch (NumberFormatException e) {
            logger.warn("Error de formato en el formulario de carrera.", e);
            // Si falla la conversión, redirige de vuelta al formulario con error
            res.redirect("/admin/carreras/nueva?error=Error: El código y la duración deben ser números.&codCarrera=" + codStr + "&nombre=" + nombre + "&duracion=" + duracionStr);
        
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Si falla una regla de negocio (ej. código duplicado)
            logger.warn("Error al crear carrera: {}", e.getMessage());
            res.redirect("/admin/carreras/nueva?error=" + e.getMessage() + "&codCarrera=" + codStr + "&nombre=" + nombre + "&duracion=" + duracionStr);
        }
        return null; // Spark maneja la redirección
    }
    
    // --- MÉTODOS PARA ACTUALIZAR Y BORRAR (Quedan como esqueleto) ---

    /**
     * [GET /admin/carreras/:id/editar]
     * Muestra el formulario para editar una carrera existente.
     */
    public ModelAndView showEditForm(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        try {
            int id = Integer.parseInt(req.params(":id"));
            Carrera carrera = carreraService.getCarreraById(id)
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada"));
            
            model.put("carrera", carrera);
            model.put("editMode", true); // Para que el formulario sepa que es edición
            model.put("errorMessage", req.queryParams("error"));
            
            return new ModelAndView(model, "admin/carreras/formulario.mustache");
            
        } catch (Exception e) {
            model.put("errorMessage", "Error al cargar la carrera: " + e.getMessage());
            return new ModelAndView(model, "admin/carreras/index.mustache"); // Vuelve al índice si hay error
        }
    }
    
    /**
     * [POST /admin/carreras/:id/editar]
     * Procesa la actualización de una carrera.
     */
    public Object updateCarrera(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        String nombre = req.queryParams("nombre");
        String duracionStr = req.queryParams("duracion");
        
        try {
            int duracion = Integer.parseInt(duracionStr);
            carreraService.updateCarrera(id, nombre, duracion);
            res.redirect("/admin/carreras?success=Carrera actualizada exitosamente.");
            
        } catch (Exception e) {
            logger.error("Error al actualizar carrera: {}", e.getMessage());
            res.redirect("/admin/carreras/" + id + "/editar?error=" + e.getMessage());
        }
        return null;
    }

    /**
     * [POST /admin/carreras/:id/eliminar]
     * Procesa la eliminación de una carrera.
     */
    public Object deleteCarrera(Request req, Response res) {
        int id = Integer.parseInt(req.params(":id"));
        try {
            // Intentamos eliminar
            carreraService.deleteCarrera(id);
            res.redirect("/admin/carreras?success=Carrera eliminada exitosamente.");
            
        } catch (IllegalStateException e) {
            // Error de REGLA DE NEGOCIO (ej. tiene hijos)
            logger.warn("Intento de eliminar carrera con dependencias: {}", e.getMessage());
            res.redirect("/admin/carreras?errorMessage=" + e.getMessage());
        } catch (Exception e) {
            // Otro error (ej. no se encontró)
            logger.error("Error al eliminar carrera: {}", e.getMessage());
            res.redirect("/admin/carreras?errorMessage=" + e.getMessage());
        }
        return null;
    }
}