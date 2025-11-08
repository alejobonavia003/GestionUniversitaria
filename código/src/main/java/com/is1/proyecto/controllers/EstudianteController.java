package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Materia;
import com.is1.proyecto.models.NotaFinal;
import com.is1.proyecto.repositories.MateriaRepository;
import com.is1.proyecto.services.EstudianteService;
import com.is1.proyecto.config.AuthMiddleware;
import com.is1.proyecto.utils.LoggerUtil;
import org.slf4j.Logger;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

/**
 * Controlador para las rutas del rol ESTUDIANTE.
 * Protegido por AuthMiddleware.requireEstudiante.
 */
public class EstudianteController {
    private static final Logger logger = LoggerUtil.getLogger(EstudianteController.class);
    private final EstudianteService estudianteService;
    private final MateriaRepository materiaRepository; // Para listar las materias

    public EstudianteController(EstudianteService estudianteService, MateriaRepository materiaRepository) {
        this.estudianteService = estudianteService;
        this.materiaRepository = materiaRepository;
    }

    /**
     * Muestra el historial académico (notas) del estudiante logueado.
     * Ruta: GET /estudiante/historial
     */
    public ModelAndView showHistorial(Request req, Response res) {
        Map<String, Object> model = AuthMiddleware.getBaseModel(req);
        long dniEstudiante = req.session().attribute("loggedUserDNI");

        List<NotaFinal> historial = estudianteService.getHistorialAcademico(dniEstudiante);

        model.put("historial", historial);
        model.put("pageTitle", "Mi Historial Académico");
        // Vista: templates/estudiante/historial.mustache
        return new ModelAndView(model, "estudiante/historial.mustache");
    }

    /**
     * Muestra la página para inscribirse a materias.
     * Ruta: GET /estudiante/inscripciones
     */
    public ModelAndView showInscripciones(Request req, Response res) {
        Map<String, Object> model = AuthMiddleware.getBaseModel(req);
        
        // Simplificación: mostramos TODAS las materias. 
        // Idealmente, aquí filtraríamos por el Plan de Estudio del alumno.
        List<Materia> materias = materiaRepository.findAll();

        model.put("materias", materias);
        model.put("pageTitle", "Inscripción a Materias");
        model.put("errorMessage", req.queryParams("error"));
        model.put("successMessage", req.queryParams("success"));
        
        // Vista: templates/estudiante/inscripciones.mustache
        return new ModelAndView(model, "estudiante/inscripciones.mustache");
    }

    /**
     * Procesa la solicitud de inscripción a una materia.
     * Ruta: POST /estudiante/inscribir
     */
    public Object procesarInscripcion(Request req, Response res) {
        long dniEstudiante = req.session().attribute("loggedUserDNI");
        String redirectTo = "/estudiante/inscripciones";
        
        try {
            long materiaId = Long.parseLong(req.queryParams("codigo_materia"));
            
            estudianteService.inscribirAMateria(dniEstudiante, materiaId);
            
            res.redirect(redirectTo + "?success=Inscripción exitosa");
        } catch (NumberFormatException e) {
            logger.warn("Intento de inscripción con ID de materia inválido.");
            res.redirect(redirectTo + "?error=ID de materia inválido.");
        } catch (Exception e) {
            logger.warn("Fallo la inscripción: {}", e.getMessage());
            res.redirect(redirectTo + "?error=" + e.getMessage());
        }
        return null;
    }
}