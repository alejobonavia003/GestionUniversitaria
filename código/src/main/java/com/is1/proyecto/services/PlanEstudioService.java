package com.is1.proyecto.services;

import com.is1.proyecto.models.Carrera;
import com.is1.proyecto.models.PlanEstudio;
import com.is1.proyecto.repositories.CarreraRepository;
import com.is1.proyecto.repositories.PlanEstudioRepository;
import com.is1.proyecto.utils.LoggerUtil;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import spark.Request;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la lógica de negocio de PlanEstudio.
 */
public class PlanEstudioService {
    private static final Logger logger = LoggerUtil.getLogger(PlanEstudioService.class);

    private final PlanEstudioRepository planEstudioRepository;
    private final CarreraRepository carreraRepository; // Para validaciones

    public PlanEstudioService(PlanEstudioRepository planEstudioRepository, CarreraRepository carreraRepository) {
        this.planEstudioRepository = planEstudioRepository;
        this.carreraRepository = carreraRepository;
    }

    public Optional<PlanEstudio> getPlanById(long id) {
        return planEstudioRepository.findById(id);
    }

    public List<PlanEstudio> getPlanesByCarreraId(int codCarrera) {
        return planEstudioRepository.findByCarreraId(codCarrera);
    }

    /**
     * Guarda (Crea o Actualiza) un Plan de Estudio a partir de una petición web.
     * @param req La petición Spark que contiene los queryParams del formulario.
     */
    public void savePlan(Request req) {
        String idParam = req.queryParams("id");
        int anioPlan = Integer.parseInt(req.queryParams("anio_plan"));
        int version = Integer.parseInt(req.queryParams("version"));
        int codCarrera = Integer.parseInt(req.queryParams("cod_carrera"));

        // Validar que la carrera exista
        if (carreraRepository.findById(codCarrera).isEmpty()) {
            throw new RuntimeException("No se puede guardar el plan: La carrera con ID " + codCarrera + " no existe.");
        }

        PlanEstudio plan;

        if (idParam != null && !idParam.isEmpty()) {
            // --- ACTUALIZACIÓN ---
            long id = Long.parseLong(idParam);
            plan = planEstudioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Plan de Estudio no encontrado para actualizar. ID: " + id));
            logger.info("Actualizando Plan de Estudio ID: {}", id);
        } else {
            // --- CREACIÓN ---
            plan = new PlanEstudio();
            logger.info("Creando nuevo Plan de Estudio para Carrera ID: {}", codCarrera);
        }

        // Seteamos los datos
        plan.set("anio_plan", anioPlan);
        plan.set("version", version);
        plan.set("cod_carrera", codCarrera);

        planEstudioRepository.save(plan);
    }

    /**
     * Elimina un Plan de Estudio, validando que no tenga Materias asociadas.
     * @param id El ID del plan a eliminar.
     */
    public void deletePlan(long id) throws Exception {
        PlanEstudio plan = planEstudioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan de Estudio no encontrado para eliminar. ID: " + id));

        // Validación de negocio: No borrar si tiene materias
        LazyList<com.is1.proyecto.models.Materia> materias = plan.getAll(com.is1.proyecto.models.Materia.class);
        if (!materias.isEmpty()) {
            logger.warn("Intento de borrado de Plan ID: {} fallido. Tiene {} materias asociadas.", id, materias.size());
            throw new Exception("No se puede eliminar: El plan tiene materias asociadas.");
        }

        planEstudioRepository.deleteById(id);
        logger.info("Plan de Estudio ID: {} eliminado exitosamente.", id);
    }
}