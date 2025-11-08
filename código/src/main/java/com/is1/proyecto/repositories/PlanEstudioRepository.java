package com.is1.proyecto.repositories;

import com.is1.proyecto.models.PlanEstudio;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el repositorio de PlanEstudio.
 * Define las operaciones de base de datos para la entidad PlanEstudio.
 */
public interface PlanEstudioRepository {

    /**
     * Busca un plan de estudio por su ID.
     * @param id El ID del plan de estudio.
     * @return Un Optional que contiene el PlanEstudio si se encuentra.
     */
    Optional<PlanEstudio> findById(long id);

    /**
     * Obtiene todos los planes de estudio asociados a un código de carrera.
     * @param codCarrera El código de la carrera.
     * @return Una lista de PlanEstudio.
     */
    List<PlanEstudio> findByCarreraId(int codCarrera);

    /**
     * Guarda un nuevo PlanEstudio o actualiza uno existente.
     * @param plan El PlanEstudio a guardar.
     */
    void save(PlanEstudio plan);

    /**
     * Elimina un PlanEstudio por su ID.
     * @param id El ID del plan a eliminar.
     * @return true si se eliminó, false en caso contrario.
     */
    boolean deleteById(long id);
}