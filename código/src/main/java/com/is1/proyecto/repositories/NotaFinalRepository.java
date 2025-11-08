package com.is1.proyecto.repositories;

import com.is1.proyecto.models.NotaFinal;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el repositorio de la entidad NotaFinal (Rindio).
 */
public interface NotaFinalRepository {

    /**
     * Busca una nota final específica por su clave compuesta.
     * @param dniEstudiante DNI del estudiante.
     * @param codigoMateria Código de la materia.
     * @return Optional con la NotaFinal.
     */
    Optional<NotaFinal> findById(long dniEstudiante, long codigoMateria);

    /**
     * Busca todas las notas de un estudiante.
     * @param dniEstudiante DNI del estudiante.
     * @return Lista de NotaFinal.
     */
    List<NotaFinal> findByEstudianteDni(long dniEstudiante);

    /**
     * Busca todas las notas cargadas para una materia.
     * @param codigoMateria Código de la materia.
     * @return Lista de NotaFinal.
     */
    List<NotaFinal> findByMateriaId(long codigoMateria);

    /**
     * Guarda (crea o actualiza) una nota final.
     * @param nota La entidad NotaFinal.
     */
    void save(NotaFinal nota);
}