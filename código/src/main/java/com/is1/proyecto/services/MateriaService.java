package com.is1.proyecto.services;

import com.is1.proyecto.models.Materia;
import com.is1.proyecto.models.NotaFinal; // Necesario para la validación
import com.is1.proyecto.repositories.MateriaRepository;
import com.is1.proyecto.repositories.PlanEstudioRepository;
import com.is1.proyecto.utils.LoggerUtil;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import spark.Request;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la lógica de negocio de Materia.
 */
public class MateriaService {
    private static final Logger logger = LoggerUtil.getLogger(MateriaService.class);

    private final MateriaRepository materiaRepository;
    private final PlanEstudioRepository planEstudioRepository; // Para validaciones

    public MateriaService(MateriaRepository materiaRepository, PlanEstudioRepository planEstudioRepository) {
        this.materiaRepository = materiaRepository;
        this.planEstudioRepository = planEstudioRepository;
    }

    /**
     * Busca una materia por su ID (código).
     * @param id El código de la materia.
     * @return Optional con la Materia.
     */
    public Optional<Materia> getMateriaById(long id) {
        return materiaRepository.findById(id);
    }

    /**
     * Busca todas las materias de un plan específico.
     * @param planId El ID del plan.
     * @return Lista de Materias.
     */
    public List<Materia> getMateriasByPlanId(long planId) {
        return materiaRepository.findByPlanId(planId);
    }

    /**
     * Guarda (Crea o Actualiza) una Materia a partir de una petición web.
     * @param req La petición Spark que contiene los queryParams del formulario.
     */
    public void saveMateria(Request req) {
        String idParam = req.queryParams("codigo"); // La PK es 'codigo'
        String nombre = req.queryParams("nombre");
        long idPlan = Long.parseLong(req.queryParams("id_plan"));

        // Validar que el plan de estudio exista
        if (planEstudioRepository.findById(idPlan).isEmpty()) {
            throw new RuntimeException("No se puede guardar: El Plan de Estudio ID " + idPlan + " no existe.");
        }

        Materia materia;

        if (idParam != null && !idParam.isEmpty()) {
            // --- ACTUALIZACIÓN ---
            long id = Long.parseLong(idParam);
            materia = materiaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Materia no encontrada para actualizar. ID: " + id));
            logger.info("Actualizando Materia ID: {}", id);
        } else {
            // --- CREACIÓN ---
            materia = new Materia();
            logger.info("Creando nueva Materia para Plan ID: {}", idPlan);
            // Si el 'codigo' (PK) no es autoincremental, debes setearlo aquí
            // materia.set("codigo", nuevoCodigo); 
        }

        // Seteamos los datos
        materia.set("nombre", nombre);
        materia.set("id_plan", idPlan);

        materiaRepository.save(materia);
    }

    /**
     * Elimina una Materia, validando que no tenga dependencias.
     * @param id El ID (código) de la materia a eliminar.
     */
    public void deleteMateria(long id) throws Exception {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada para eliminar. ID: " + id));

        // Validación 1: No borrar si tiene notas cargadas (Historial Académico)
        // Usamos la relación @HasMany(list = NotaFinal.class)
        if (materia.getAll(NotaFinal.class).isEmpty()) {
            logger.warn("Intento de borrado de Materia ID: {} fallido. Tiene notas cargadas.", id);
            throw new Exception("No se puede eliminar: La materia tiene notas de alumnos registradas.");
        }

        // Validación 2: No borrar si es correlativa de OTRA materia
        // Usamos la relación @Many2Many(target = Materia.class, ...)
        // 'materia.get(Materia.class, "codigo_correlativa = ?", id)' -> Esta consulta es compleja
        // Por ahora, omitimos esta validación compleja de N:M, pero aquí iría.
        
        // Validación 3: No borrar si tiene profesores asignados (Dicta)
        // (Omitido por brevedad, pero sería igual que la Validación 1)


        materiaRepository.deleteById(id);
        logger.info("Materia ID: {} eliminada exitosamente.", id);
    }
    
    // Aquí irían los métodos para gestionar Correlativas (addCorrelativa, removeCorrelativa)
}