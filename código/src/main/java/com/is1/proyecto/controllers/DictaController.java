package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Materia;
import com.is1.proyecto.models.Profesor;
import com.is1.proyecto.models.Periodo;
import com.is1.proyecto.services.DictaService;
import com.is1.proyecto.services.MateriaService;
import com.is1.proyecto.services.UsuarioService; // Para listar profesores
import com.is1.proyecto.repositories.ProfesorRepository; // Necesario para listar
import com.is1.proyecto.repositories.PeriodoRepository; // Necesario para listar
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

/**
 * Controlador para gestionar las asignaciones de Profesores a Materias (Dicta).
 * Protegido por rol de ADMIN.
 */
public class DictaController {

    private static final Logger logger = LoggerUtil.getLogger(DictaController.class);
    private final DictaService dictaService;
    private final MateriaService materiaService;
    // Necesitamos los repositorios (o servicios) de Profesor y Periodo para poblar los formularios
    private final ProfesorRepository profesorRepository;
    private final PeriodoRepository periodoRepository;


    public DictaController(DictaService dictaService, MateriaService materiaService, ProfesorRepository profesorRepository, PeriodoRepository periodoRepository) {
        this.dictaService = dictaService;
        this.materiaService = materiaService;
        this.profesorRepository = profesorRepository;
        this.periodoRepository = periodoRepository;
    }

    /**
     * Muestra el panel de gestión de asignaciones para una materia específica.
     * Carga la materia, sus asignaciones actuales, y listas de profesores/periodos para el formulario.
     * Ruta: GET /admin/materias/:id/asignaciones
     */
    public ModelAndView showAsignacionForm(Request req, Response res) {
        Map<String, Object> model = AuthMiddleware.getBaseModel(req);
        long materiaId = Long.parseLong(req.params("id"));

        Optional<Materia> materiaOpt = materiaService.getMateriaById(materiaId);
        if (materiaOpt.isEmpty()) {
            res.redirect("/admin/carreras?error=Materia no encontrada");
            return null;
        }

        // Datos para la vista
        model.put("materia", materiaOpt.get());
        model.put("asignacionesActuales", dictaService.getAsignacionesPorMateria(materiaId));
        
        // Datos para poblar los <select> del formulario
        model.put("listaProfesores", profesorRepository.findAll()); // ¡Necesitamos este método en el repo!
        model.put("listaPeriodos", periodoRepository.findAll());

        model.put("pageTitle", "Asignar Docentes a " + materiaOpt.get().getString("nombre"));
        model.put("errorMessage", req.queryParams("error"));
        model.put("successMessage", req.queryParams("success"));

        // Vista: templates/admin/asignaciones/formulario.mustache (la crearemos en el futuro)
        return new ModelAndView(model, "admin/asignaciones/formulario.mustache");
    }

    /**
     * Procesa el formulario para ASIGNAR un profesor.
     * Ruta: POST /admin/asignaciones/guardar
     */
    public Object asignarProfesor(Request req, Response res) {
        // Obtenemos el ID de la materia para la redirección
        String materiaId = req.queryParams("codigo_materia");
        String redirectTo = "/admin/materias/" + materiaId + "/asignaciones";

        try {
            dictaService.asignarProfesor(req);
            res.redirect(redirectTo + "?success=Profesor asignado exitosamente");
        } catch (Exception e) {
            logger.warn("Error al asignar profesor: {}", e.getMessage());
            res.redirect(redirectTo + "?error=" + e.getMessage());
        }
        return null;
    }

    /**
     * Procesa la eliminación (desasignación) de un profesor.
     * Ruta: POST /admin/asignaciones/eliminar
     */
    public Object desasignarProfesor(Request req, Response res) {
        long dniProf = Long.parseLong(req.queryParams("dni_prof"));
        long codMateria = Long.parseLong(req.queryParams("codigo_materia"));
        long idPeriodo = Long.parseLong(req.queryParams("id_periodo"));
        
        String redirectTo = "/admin/materias/" + codMateria + "/asignaciones";

        try {
            dictaService.desasignarProfesor(dniProf, codMateria, idPeriodo);
            res.redirect(redirectTo + "?success=Asignación eliminada");
        } catch (Exception e) {
            logger.warn("Error al desasignar profesor: {}", e.getMessage());
            res.redirect(redirectTo + "?error=" + e.getMessage());
        }
        return null;
    }
}