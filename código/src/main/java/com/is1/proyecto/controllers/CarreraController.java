package com.is1.proyecto.controllers;

import com.is1.proyecto.services.CarreraService;
import com.is1.proyecto.dto.CarreraDTO;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import static spark.Spark.*;

/**
 * Controlador que maneja las peticiones HTTP relacionadas con Carreras.
 */
public class CarreraController {
    private final CarreraService carreraService;
    private final MustacheTemplateEngine templateEngine;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
        this.templateEngine = new MustacheTemplateEngine();
        setupRoutes();
    }

    private void setupRoutes() {
        // Listar carreras
        get("/carreras", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            try {
                List<CarreraDTO> carreras = carreraService.getAllCarreras();
                model.put("carreras", carreras);
                
                // Agregar breadcrumbs
                List<Map<String, String>> breadcrumbs = new ArrayList<>();
                Map<String, String> breadcrumb = new HashMap<>();
                breadcrumb.put("text", "Carreras");
                breadcrumb.put("active", "true");
                breadcrumbs.add(breadcrumb);
                model.put("breadcrumbs", breadcrumbs);
                
                return templateEngine.render(new ModelAndView(model, "carreras_new.mustache"));
            } catch (Exception e) {
                res.status(500);
                model.put("error", "Error al cargar las carreras: " + e.getMessage());
                return templateEngine.render(new ModelAndView(model, "error.mustache"));
            }
        });

        // Formulario nueva carrera
        get("/carreras/nueva", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            
            // Agregar breadcrumbs
            List<Map<String, String>> breadcrumbs = new ArrayList<>();
            Map<String, String> carrerasBC = new HashMap<>();
            carrerasBC.put("text", "Carreras");
            carrerasBC.put("url", "/carreras");
            breadcrumbs.add(carrerasBC);
            
            Map<String, String> currentBC = new HashMap<>();
            currentBC.put("text", "Nueva Carrera");
            currentBC.put("active", "true");
            breadcrumbs.add(currentBC);
            
            model.put("breadcrumbs", breadcrumbs);
            
            return templateEngine.render(new ModelAndView(model, "carrera_form_new.mustache"));
        });

        // Crear carrera
        post("/carreras", (req, res) -> {
            try {
                CarreraDTO carreraDTO = new CarreraDTO();
                carreraDTO.setCodigo(Integer.parseInt(req.queryParams("codigo")));
                carreraDTO.setNombre(req.queryParams("nombre"));
                carreraDTO.setDuracion(Integer.parseInt(req.queryParams("duracion")));
                
                carreraService.createCarrera(carreraDTO);
                res.redirect("/carreras");
                return null;
            } catch (Exception e) {
                Map<String, Object> model = new HashMap<>();
                model.put("error", "Error al crear la carrera: " + e.getMessage());
                return templateEngine.render(new ModelAndView(model, "error.mustache"));
            }
        });
    }
}