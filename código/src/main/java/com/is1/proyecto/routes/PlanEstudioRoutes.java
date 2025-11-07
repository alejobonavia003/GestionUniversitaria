package com.is1.proyecto.routes;

import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import com.is1.proyecto.models.carrera.PlanDeEstudio;
import com.is1.proyecto.models.carrera.Carrera;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class PlanEstudioRoutes {
    public static void configure() {
        // Listar planes de estudio
        get("/planes", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<PlanDeEstudio> planes = PlanDeEstudio.findAll();
            model.put("planes", planes);
            return new ModelAndView(model, "planes.mustache");
        }, new MustacheTemplateEngine());

        // Mostrar formulario para nuevo plan
        get("/planes/nuevo", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Carrera> carreras = Carrera.findAll();
            model.put("carreras", carreras);
            return new ModelAndView(model, "plan_form.mustache");
        }, new MustacheTemplateEngine());

        // Crear nuevo plan
        post("/planes", (req, res) -> {
            String codCarrera = req.queryParams("cod_carrera");
            int anioPlan = Integer.parseInt(req.queryParams("anio_plan"));
            int version = Integer.parseInt(req.queryParams("version"));

            PlanDeEstudio plan = new PlanDeEstudio();
            plan.set("cod_carrera", codCarrera);
            plan.set("anio_plan", anioPlan);
            plan.set("version", version);
            plan.saveIt();

            res.redirect("/planes");
            return null;
        });

        // Mostrar formulario para editar plan
        get("/planes/:id/editar", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            PlanDeEstudio plan = PlanDeEstudio.findById(id);
            
            if (plan == null) {
                res.status(404);
                return new ModelAndView(new HashMap<>(), "error.mustache");
            }

            Map<String, Object> model = new HashMap<>();
            model.put("plan", plan);
            List<Carrera> carreras = Carrera.findAll();
            model.put("carreras", carreras);
            return new ModelAndView(model, "plan_form.mustache");
        }, new MustacheTemplateEngine());

        // Actualizar plan
        post("/planes/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            PlanDeEstudio plan = PlanDeEstudio.findById(id);
            
            if (plan != null) {
                plan.set("cod_carrera", req.queryParams("cod_carrera"));
                plan.set("anio_plan", Integer.parseInt(req.queryParams("anio_plan")));
                plan.set("version", Integer.parseInt(req.queryParams("version")));
                plan.saveIt();
            }

            res.redirect("/planes");
            return null;
        });

        // Eliminar plan
        delete("/planes/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            PlanDeEstudio plan = PlanDeEstudio.findById(id);
            
            if (plan != null) {
                plan.delete();
            }

            res.redirect("/planes");
            return null;
        });
    }
}