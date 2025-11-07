package com.is1.proyecto.routes;

import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import com.is1.proyecto.models.carrera.Carrera;
import com.is1.proyecto.config.AuthMiddleware;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class CarreraRoutes {
    public static void configure() {
        before("/carreras/*", AuthMiddleware.requireAdmin);
        before("/carreras", AuthMiddleware.requireAdmin);

        // Mostrar lista de carreras
        get("/carreras", (req, res) -> {
            Map<String, Object> model = AuthMiddleware.getBaseModel(req);
            List<Carrera> carreras = Carrera.findAll();
            model.put("carreras", carreras);
            
            // Agregar breadcrumbs
            List<Map<String, String>> breadcrumbs = new ArrayList<>();
            Map<String, String> breadcrumb = new HashMap<>();
            breadcrumb.put("text", "Carreras");
            breadcrumb.put("active", "true");
            breadcrumbs.add(breadcrumb);
            model.put("breadcrumbs", breadcrumbs);
            
            return new ModelAndView(model, "carreras_new.mustache");
        }, new MustacheTemplateEngine());

        // Mostrar formulario para nueva carrera
        get("/carreras/nueva", (req, res) -> {
            Map<String, Object> model = AuthMiddleware.getBaseModel(req);
            
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
            
            return new ModelAndView(model, "carrera_form_new.mustache");
        }, new MustacheTemplateEngine());

        // Crear nueva carrera
        post("/carreras", (req, res) -> {
            try {
                String codCarrera = req.queryParams("cod_carrera");
                String nombre = req.queryParams("nombre");
                int duracion = Integer.parseInt(req.queryParams("duracion"));

                Carrera carrera = new Carrera();
                carrera.set("cod_carrera", codCarrera);
                carrera.set("nombre", nombre);
                carrera.set("duracion", duracion);
                carrera.saveIt();

                res.redirect("/carreras");
                return null;
            } catch (Exception e) {
                res.status(400);
                Map<String, Object> model = AuthMiddleware.getBaseModel(req);
                model.put("errorMessage", "Error al crear la carrera: " + e.getMessage());
                return new ModelAndView(model, "error.mustache");
            }
        });

        // Mostrar formulario para editar carrera
        get("/carreras/:cod/editar", (req, res) -> {
            Map<String, Object> model = AuthMiddleware.getBaseModel(req);
            String codCarrera = req.params(":cod");
            Carrera carrera = Carrera.findFirst("cod_carrera = ?", codCarrera);
            
            if (carrera == null) {
                res.status(404);
                model.put("errorMessage", "Carrera no encontrada");
                return new ModelAndView(model, "error.mustache");
            }

            model.put("carrera", carrera);
            
            // Agregar breadcrumbs
            List<Map<String, String>> breadcrumbs = new ArrayList<>();
            Map<String, String> carrerasBC = new HashMap<>();
            carrerasBC.put("text", "Carreras");
            carrerasBC.put("url", "/carreras");
            breadcrumbs.add(carrerasBC);
            
            Map<String, String> currentBC = new HashMap<>();
            currentBC.put("text", "Editar " + carrera.getString("nombre"));
            currentBC.put("active", "true");
            breadcrumbs.add(currentBC);
            
            model.put("breadcrumbs", breadcrumbs);
            
            return new ModelAndView(model, "carrera_form_new.mustache");
        }, new MustacheTemplateEngine());

        // Actualizar carrera
        post("/carreras/:cod", (req, res) -> {
            try {
                String codCarrera = req.params(":cod");
                Carrera carrera = Carrera.findFirst("cod_carrera = ?", codCarrera);
                
                if (carrera != null) {
                    carrera.set("nombre", req.queryParams("nombre"));
                    carrera.set("duracion", Integer.parseInt(req.queryParams("duracion")));
                    carrera.saveIt();
                }

                res.redirect("/carreras");
                return null;
            } catch (Exception e) {
                res.status(400);
                Map<String, Object> model = AuthMiddleware.getBaseModel(req);
                model.put("errorMessage", "Error al actualizar la carrera: " + e.getMessage());
                return new ModelAndView(model, "error.mustache");
            }
        });

        // Eliminar carrera
        delete("/carreras/:cod", (req, res) -> {
            try {
                String codCarrera = req.params(":cod");
                Carrera carrera = Carrera.findFirst("cod_carrera = ?", codCarrera);
                
                if (carrera != null) {
                    carrera.delete();
                }

                res.redirect("/carreras");
                return null;
            } catch (Exception e) {
                res.status(400);
                Map<String, Object> model = AuthMiddleware.getBaseModel(req);
                model.put("errorMessage", "Error al eliminar la carrera: " + e.getMessage());
                return new ModelAndView(model, "error.mustache");
            }
        });

        // Mostrar formulario para nueva carrera
        get("/carreras/nueva", (req, res) -> {
            return new ModelAndView(new HashMap<>(), "carrera_form.mustache");
        }, new MustacheTemplateEngine());

        // Crear nueva carrera
        post("/carreras", (req, res) -> {
            String codCarrera = req.queryParams("cod_carrera");
            String nombre = req.queryParams("nombre");
            int duracion = Integer.parseInt(req.queryParams("duracion"));

            Carrera carrera = new Carrera();
            carrera.set("cod_carrera", codCarrera);
            carrera.set("nombre", nombre);
            carrera.set("duracion", duracion);
            carrera.saveIt();

            res.redirect("/carreras");
            return null;
        });

        // Mostrar formulario para editar carrera
        get("/carreras/:cod/editar", (req, res) -> {
            String codCarrera = req.params(":cod");
            Carrera carrera = Carrera.findFirst("cod_carrera = ?", codCarrera);
            
            if (carrera == null) {
                res.status(404);
                return new ModelAndView(new HashMap<>(), "error.mustache");
            }

            Map<String, Object> model = new HashMap<>();
            model.put("carrera", carrera);
            return new ModelAndView(model, "carrera_form.mustache");
        }, new MustacheTemplateEngine());

        // Actualizar carrera
        post("/carreras/:cod", (req, res) -> {
            String codCarrera = req.params(":cod");
            Carrera carrera = Carrera.findFirst("cod_carrera = ?", codCarrera);
            
            if (carrera != null) {
                carrera.set("nombre", req.queryParams("nombre"));
                carrera.set("duracion", Integer.parseInt(req.queryParams("duracion")));
                carrera.saveIt();
            }

            res.redirect("/carreras");
            return null;
        });

        // Eliminar carrera
        delete("/carreras/:cod", (req, res) -> {
            String codCarrera = req.params(":cod");
            Carrera carrera = Carrera.findFirst("cod_carrera = ?", codCarrera);
            
            if (carrera != null) {
                carrera.delete();
            }

            res.redirect("/carreras");
            return null;
        });
    }
}