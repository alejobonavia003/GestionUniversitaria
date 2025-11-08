package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Materia;
import com.is1.proyecto.models.PlanEstudio;
import com.is1.proyecto.services.MateriaService;
import com.is1.proyecto.services.PlanEstudioService;
import com.is1.proyecto.config.AuthMiddleware; // ¡Usamos el helper!
import com.is1.proyecto.utils.LoggerUtil;
import org.slf4j.Logger;
import spark.ModelAndView;
import spark.Request;
import spark.Response;


import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador para la gestión de Materias (CRUD).
 * Protegido por rol de ADMIN.
 */
public class MateriaController {

    private static final Logger logger = LoggerUtil.getLogger(MateriaController.class);
    private final MateriaService materiaService;
    private final PlanEstudioService planEstudioService;

    public MateriaController(MateriaService materiaService, PlanEstudioService planEstudioService) {
        this.materiaService = materiaService;
        this.planEstudioService = planEstudioService;
    }

    /**
     * Muestra la lista de materias para un plan de estudio específico.
     * Ruta: GET /admin/planes/:plan_id/materias
     */
    public ModelAndView listMateriasByPlan(Request req, Response res) {
        Map<String, Object> model = AuthMiddleware.getBaseModel(req);
        long planId = Long.parseLong(req.params("plan_id"));

        Optional<PlanEstudio> planOpt = planEstudioService.getPlanById(planId);
        if (planOpt.isEmpty()) {
            res.redirect("/admin/carreras?error=Plan de estudio no encontrado");
            return null;
        }

        PlanEstudio plan = planOpt.get();
        List<Materia> materias = materiaService.getMateriasByPlanId(planId);

        model.put("plan", plan);
        model.put("carrera", plan.parent(com.is1.proyecto.models.Carrera.class)); // Para los breadcrumbs
        model.put("materias", materias);
        model.put("pageTitle", "Materias del Plan " + plan.getAnioPlan());
        
        // Mensajes de feedback
        model.put("successMessage", req.queryParams("success"));
        model.put("errorMessage", req.queryParams("error"));

        // Vista: templates/admin/materias/index.mustache (la crearemos en el futuro)
        return new ModelAndView(model, "admin/materias/index.mustache");
    }

    /**
     * Muestra el formulario para crear una nueva Materia (asociada a un plan).
     * Ruta: GET /admin/planes/:plan_id/materias/nueva
     */
    public ModelAndView showCreateForm(Request req, Response res) {
        Map<String, Object> model = AuthMiddleware.getBaseModel(req);
        long planId = Long.parseLong(req.params("plan_id"));

        Optional<PlanEstudio> planOpt = planEstudioService.getPlanById(planId);
        if (planOpt.isEmpty()) {
            res.redirect("/admin/carreras?error=Plan de estudio no encontrado");
            return null;
        }

        model.put("plan", planOpt.get());
        model.put("materia", new Materia()); // Objeto vacío
        model.put("editMode", false);
        model.put("pageTitle", "Nueva Materia");

        // Vista: templates/admin/materias/formulario.mustache
        return new ModelAndView(model, "admin/materias/formulario.mustache");
    }

    /**
     * Muestra el formulario para editar una Materia existente.
     * Ruta: GET /admin/materias/:id/editar
     */
    public ModelAndView showEditForm(Request req, Response res) {
        Map<String, Object> model = AuthMiddleware.getBaseModel(req);
        long materiaId = Long.parseLong(req.params("id"));

        Optional<Materia> materiaOpt = materiaService.getMateriaById(materiaId);
        if (materiaOpt.isEmpty()) {
            res.redirect("/admin/carreras?error=Materia no encontrada");
            return null;
        }

        Materia materia = materiaOpt.get();
        PlanEstudio plan = materia.parent(PlanEstudio.class);

        model.put("plan", plan);
        model.put("materia", materia);
        model.put("editMode", true);
        model.put("pageTitle", "Editar Materia");

        return new ModelAndView(model, "admin/materias/formulario.mustache");
    }

    /**
     * Procesa el guardado (Crear o Actualizar) de una Materia.
     * Ruta: POST /admin/materias/guardar
     */
    public Object saveMateria(Request req, Response res) {
        // Necesitamos el id_plan para la redirección
        long planId = Long.parseLong(req.queryParams("id_plan"));

        try {
            materiaService.saveMateria(req);
            res.redirect("/admin/planes/" + planId + "/materias?success=Materia guardada");
        } catch (Exception e) {
            logger.error("Error al guardar la materia: {}", e.getMessage(), e);
            // Si falla, redirige al formulario de creación (deberíamos mejorar esto para editar)
            res.redirect("/admin/planes/" + planId + "/materias/nueva?error=Error al guardar: " + e.getMessage());
        }
        return null;
    }

    /**
     * Elimina una Materia.
     * Ruta: POST /admin/materias/:id/eliminar
     */
    public Object deleteMateria(Request req, Response res) {
        long materiaId = Long.parseLong(req.params("id"));
        
        // Obtenemos el plan ANTES de borrar, para la redirección
        Optional<Materia> materiaOpt = materiaService.getMateriaById(materiaId);
        if (materiaOpt.isEmpty()) {
            res.redirect("/admin/carreras?error=Materia no encontrada");
            return null;
        }
        
        long planId = materiaOpt.get().getLong("id_plan");

        try {
            materiaService.deleteMateria(materiaId);
            res.redirect("/admin/planes/" + planId + "/materias?success=Materia eliminada");
        } catch (Exception e) {
            // Captura la validación de negocio (ej. "tiene notas cargadas")
            logger.warn("Error al eliminar materia ID: {}: {}", materiaId, e.getMessage());
            res.redirect("/admin/planes/" + planId + "/materias?error=" + e.getMessage());
        }
        return null;
    }
}