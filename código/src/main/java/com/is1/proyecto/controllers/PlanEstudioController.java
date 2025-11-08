package com.is1.proyecto.controllers;

import com.is1.proyecto.models.Carrera;
import com.is1.proyecto.models.PlanEstudio;
import com.is1.proyecto.services.CarreraService;
import com.is1.proyecto.services.PlanEstudioService;
import com.is1.proyecto.config.AuthMiddleware; // Asegúrate de importar tu middleware
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
 * Controlador para la gestión de Planes de Estudio (CRUD).
 * Todas las rutas aquí deben ser protegidas por rol de ADMIN.
 */
public class PlanEstudioController {

    private static final Logger logger = LoggerUtil.getLogger(PlanEstudioController.class);
    private final PlanEstudioService planEstudioService;
    private final CarreraService carreraService;

    public PlanEstudioController(PlanEstudioService planEstudioService, CarreraService carreraService) {
        this.planEstudioService = planEstudioService;
        this.carreraService = carreraService;
    }

    /**
     * Muestra la lista de planes de estudio para una carrera específica.
     * Ruta: GET /admin/carreras/:carrera_id/planes
     */
    public ModelAndView listPlanesByCarrera(Request req, Response res) {
        Map<String, Object> model = AuthMiddleware.getBaseModel(req); // Obtiene info de sesión
        int carreraId = Integer.parseInt(req.params("carrera_id"));
        
        Optional<Carrera> carreraOpt = carreraService.getCarreraById(carreraId);
        if (carreraOpt.isEmpty()) {
            logger.warn("Intento de acceso a planes de una carrera inexistente. ID: {}", carreraId);
            res.redirect("/admin/carreras?error=Carrera no encontrada");
            return null;
        }

        Carrera carrera = carreraOpt.get();
        List<PlanEstudio> planes = planEstudioService.getPlanesByCarreraId(carreraId);

        model.put("carrera", carrera);
        model.put("planes", planes);
        model.put("pageTitle", "Planes de Estudio de " + carrera.getNombre());
        // Lógica de Breadcrumbs (adaptada a tu sistema)
        // model.put("breadcrumbs", ...); 

        // Suponiendo una vista en 'templates/admin/planes/index.mustache'
        return new ModelAndView(model, "admin/planes/index.mustache");
    }

    /**
     * Muestra el formulario para crear un nuevo Plan de Estudio para una carrera.
     * Ruta: GET /admin/carreras/:carrera_id/planes/nuevo
     */
    public ModelAndView showCreateForm(Request req, Response res) {
        Map<String, Object> model = AuthMiddleware.getBaseModel(req);
        int carreraId = Integer.parseInt(req.params("carrera_id"));

        Optional<Carrera> carreraOpt = carreraService.getCarreraById(carreraId);
        if (carreraOpt.isEmpty()) {
            res.redirect("/admin/carreras?error=Carrera no encontrada");
            return null;
        }

        model.put("carrera", carreraOpt.get());
        model.put("plan", new PlanEstudio()); // Objeto vacío para el formulario
        model.put("editMode", false);
        model.put("pageTitle", "Nuevo Plan de Estudio");
        
        // Suponiendo una vista en 'templates/admin/planes/formulario.mustache'
        return new ModelAndView(model, "admin/planes/formulario.mustache");
    }

    /**
     * Muestra el formulario para editar un Plan de Estudio existente.
     * Ruta: GET /admin/planes/:id/editar
     */
    public ModelAndView showEditForm(Request req, Response res) {
        Map<String, Object> model = AuthMiddleware.getBaseModel(req);
        long planId = Long.parseLong(req.params("id"));

        Optional<PlanEstudio> planOpt = planEstudioService.getPlanById(planId);
        if (planOpt.isEmpty()) {
            res.redirect("/admin/carreras?error=Plan de estudio no encontrado");
            return null;
        }

        PlanEstudio plan = planOpt.get();
        Carrera carrera = plan.parent(Carrera.class); // Obtiene la carrera padre

        model.put("carrera", carrera);
        model.put("plan", plan);
        model.put("editMode", true);
        model.put("pageTitle", "Editar Plan de Estudio");

        return new ModelAndView(model, "admin/planes/formulario.mustache");
    }

    /**
     * Procesa el formulario para guardar (Crear o Actualizar) un Plan de Estudio.
     * Ruta: POST /admin/planes/guardar
     */
    public Object savePlan(Request req, Response res) {
        // Obtenemos el ID de la carrera (necesario para la redirección)
        int carreraId = Integer.parseInt(req.queryParams("cod_carrera"));

        try {
            planEstudioService.savePlan(req); // El servicio se encarga de la lógica
            logger.info("Plan de estudio guardado exitosamente para la carrera ID: {}", carreraId);
            res.redirect("/admin/carreras/" + carreraId + "/planes?success=Plan guardado");
        } catch (Exception e) {
            logger.error("Error al guardar el plan de estudio: {}", e.getMessage(), e);
            res.redirect("/admin/carreras/" + carreraId + "/planes?error=Error al guardar: " + e.getMessage());
        }
        return null;
    }

    /**
     * Elimina un Plan de Estudio.
     * Ruta: GET /admin/planes/:id/eliminar
     */
    public Object deletePlan(Request req, Response res) {
        long planId = Long.parseLong(req.params("id"));
        
        // Obtenemos la carrera ANTES de borrar el plan, para saber a dónde redirigir
        Optional<PlanEstudio> planOpt = planEstudioService.getPlanById(planId);
        if (planOpt.isEmpty()) {
             res.redirect("/admin/carreras?error=Plan no encontrado");
             return null;
        }
        
        int carreraId = planOpt.get().getInteger("cod_carrera");

        try {
            planEstudioService.deletePlan(planId);
            logger.info("Plan de estudio ID: {} eliminado.", planId);
            res.redirect("/admin/carreras/" + carreraId + "/planes?success=Plan eliminado");
        } catch (Exception e) {
            logger.error("Error al eliminar plan de estudio ID: {}: {}", planId, e.getMessage(), e);
            res.redirect("/admin/carreras/" + carreraId + "/planes?error=Error al eliminar: " + e.getMessage());
        }
        return null;
    }
}