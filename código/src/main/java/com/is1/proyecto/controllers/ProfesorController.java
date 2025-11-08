package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Cursa;
import com.is1.proyecto.models.Dicta;
import com.is1.proyecto.models.Materia;
import com.is1.proyecto.models.NotaFinal;
import com.is1.proyecto.repositories.MateriaRepository;
import com.is1.proyecto.services.ProfesorService;
import com.is1.proyecto.config.AuthMiddleware;
import com.is1.proyecto.utils.LoggerUtil;
import org.slf4j.Logger;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador para las rutas del rol PROFESOR.
 * Protegido por AuthMiddleware.requireProfesor.
 */
public class ProfesorController {
    private static final Logger logger = LoggerUtil.getLogger(ProfesorController.class);
    private final ProfesorService profesorService;
    private final MateriaRepository materiaRepository; // Para buscar detalles de la materia

    public ProfesorController(ProfesorService profesorService, MateriaRepository materiaRepository) {
        this.profesorService = profesorService;
        this.materiaRepository = materiaRepository;
    }

    /**
     * Muestra las materias asignadas al profesor logueado.
     * Ruta: GET /profesor/mis-materias
     */
    public ModelAndView showMisMaterias(Request req, Response res) {
        Map<String, Object> model = AuthMiddleware.getBaseModel(req);
        
        // ¡IMPORTANTE! Asumimos que el DNI del profesor se guarda en la sesión con esta clave
        long dniProfesor = req.session().attribute("loggedUserDNI");

        List<Dicta> asignaciones = profesorService.getMisMateriasAsignadas(dniProfesor);

        model.put("asignaciones", asignaciones);
        model.put("pageTitle", "Mis Materias Asignadas");
        // Vista: templates/profesor/mis-materias.mustache
        return new ModelAndView(model, "profesor/mis-materias.mustache");
    }

    /**
     * Muestra la planilla para cargar notas de una materia específica.
     * Carga los alumnos inscritos (Cursa) y sus notas actuales (NotaFinal).
     * Ruta: GET /profesor/materia/:id/notas
     */
    public ModelAndView showCargarNotas(Request req, Response res) {
        Map<String, Object> model = AuthMiddleware.getBaseModel(req);
        long materiaId = Long.parseLong(req.params("id"));

        Optional<Materia> materiaOpt = materiaRepository.findById(materiaId);
        if (materiaOpt.isEmpty()) {
            res.redirect("/profesor/mis-materias?error=Materia no encontrada");
            return null;
        }

        // 1. Obtener los alumnos inscriptos (Cursa)
        List<Cursa> inscripciones = profesorService.getAlumnosPorMateria(materiaId);
        
        // (Opcional pero recomendado: Cargar las notas actuales para mostrarlas en el formulario)
        // Esto es una lógica más compleja que podemos añadir después, 
        // por ahora solo listamos los alumnos.

        model.put("materia", materiaOpt.get());
        model.put("inscripciones", inscripciones); // La vista iterará sobre esto
        model.put("pageTitle", "Cargar Notas de " + materiaOpt.get().getString("nombre"));
        model.put("errorMessage", req.queryParams("error"));
        model.put("successMessage", req.queryParams("success"));

        // Vista: templates/profesor/cargar-notas.mustache
        return new ModelAndView(model, "profesor/cargar-notas.mustache");
    }

    /**
     * Procesa el formulario de carga de nota.
     * Ruta: POST /profesor/notas/guardar
     */
    public Object procesarCargarNotas(Request req, Response res) {
        String materiaId = req.queryParams("codigo_materia");
        String redirectTo = "/profesor/materia/" + materiaId + "/notas";

        try {
            // El servicio maneja la lógica de buscar/crear/actualizar la nota
            profesorService.cargarNota(req);
            res.redirect(redirectTo + "?success=Nota guardada exitosamente");
        } catch (Exception e) {
            logger.warn("Error al cargar nota: {}", e.getMessage());
            res.redirect(redirectTo + "?error=" + e.getMessage());
        }
        return null;
    }
}