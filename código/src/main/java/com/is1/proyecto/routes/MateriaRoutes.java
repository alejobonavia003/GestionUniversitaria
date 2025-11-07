package com.is1.proyecto.routes;

import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import com.is1.proyecto.models.carrera.Materia;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class MateriaRoutes {
    public static void configure() {
        // Listar materias
        get("/materias", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Materia> materias = Materia.findAll();
            model.put("materias", materias);
            return new ModelAndView(model, "materias.mustache");
        }, new MustacheTemplateEngine());

        // Mostrar formulario para nueva materia
        get("/materias/nueva", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Materia> materiasExistentes = Materia.findAll();
            model.put("materias", materiasExistentes); // Para seleccionar correlativas
            return new ModelAndView(model, "materia_form.mustache");
        }, new MustacheTemplateEngine());

        // Crear nueva materia
        post("/materias", (req, res) -> {
            String codMateria = req.queryParams("codigo_materia");
            String nombre = req.queryParams("nombre");

            Materia materia = new Materia();
            materia.set("codigo_materia", codMateria);
            materia.set("nombre", nombre);
            materia.saveIt();

            // Manejar correlativas si se seleccionaron
            String[] correlativas = req.queryParamsValues("correlativas");
            if (correlativas != null) {
                for (String correlativa : correlativas) {
                    materia.addCorrelativa(correlativa);
                }
            }

            res.redirect("/materias");
            return null;
        });

        // Mostrar formulario para editar materia
        get("/materias/:codigo/editar", (req, res) -> {
            String codMateria = req.params(":codigo");
            Materia materia = Materia.findFirst("codigo_materia = ?", codMateria);
            
            if (materia == null) {
                res.status(404);
                return new ModelAndView(new HashMap<>(), "error.mustache");
            }

            Map<String, Object> model = new HashMap<>();
            model.put("materia", materia);
            List<Materia> materiasExistentes = Materia.findAll();
            model.put("materias", materiasExistentes);
            List<String> correlativasActuales = materia.getCorrelativas();
            model.put("correlativasActuales", correlativasActuales);
            return new ModelAndView(model, "materia_form.mustache");
        }, new MustacheTemplateEngine());

        // Actualizar materia
        post("/materias/:codigo", (req, res) -> {
            String codMateria = req.params(":codigo");
            Materia materia = Materia.findFirst("codigo_materia = ?", codMateria);
            
            if (materia != null) {
                materia.set("nombre", req.queryParams("nombre"));
                materia.saveIt();

                // Actualizar correlativas
                materia.removeAllCorrelativas();
                String[] correlativas = req.queryParamsValues("correlativas");
                if (correlativas != null) {
                    for (String correlativa : correlativas) {
                        materia.addCorrelativa(correlativa);
                    }
                }
            }

            res.redirect("/materias");
            return null;
        });

        // Eliminar materia
        delete("/materias/:codigo", (req, res) -> {
            String codMateria = req.params(":codigo");
            Materia materia = Materia.findFirst("codigo_materia = ?", codMateria);
            
            if (materia != null) {
                materia.removeAllCorrelativas(); // Primero eliminar las correlativas
                materia.delete();
            }

            res.redirect("/materias");
            return null;
        });
    }
}