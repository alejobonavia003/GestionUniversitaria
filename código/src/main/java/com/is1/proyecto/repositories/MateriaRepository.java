package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Materia;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el repositorio de Materia.
 */
public interface MateriaRepository {

    /**
     * Busca una materia por su ID (código).
     * @param id El código de la materia.
     * @return Un Optional que contiene la Materia.
     */
    Optional<Materia> findById(long id);

    /**
     * Obtiene todas las materias de un plan de estudio específico.
     * @param planId El ID del plan de estudio.
     * @return Una lista de Materias.
     */
    List<Materia> findByPlanId(long planId);
    
    /**
     * Obtiene todas las materias de la base de datos.
     * @return Una lista de todas las Materias.
     */
    List<Materia> findAll();

    /**
     * Guarda (crea o actualiza) una materia.
     * @param materia La entidad Materia.
     */
    void save(Materia materia);

    /**
     * Elimina una materia por su ID (código).
     * @param id El código de la materia.
     * @return true si se eliminó.
     */
    boolean deleteById(long id);
}