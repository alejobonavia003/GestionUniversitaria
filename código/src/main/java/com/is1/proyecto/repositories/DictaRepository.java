package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Dicta;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el repositorio de la entidad Dicta (Asignación Docente).
 */
public interface DictaRepository {

    /**
     * Busca todas las asignaciones de una materia específica.
     * @param codigoMateria El código de la materia.
     * @return Lista de asignaciones (Dicta).
     */
    List<Dicta> findByMateriaId(long codigoMateria);

    /**
     * Busca todas las asignaciones de un profesor específico.
     * @param dniProfesor El DNI del profesor.
     * @return Lista de asignaciones (Dicta).
     */
    List<Dicta> findByProfesorDni(long dniProfesor);

    /**
     * Guarda (crea o actualiza) una asignación.
     * @param asignacion La entidad Dicta.
     */
    void save(Dicta asignacion);

    /**
     * Elimina una asignación específica por su clave compuesta.
     * @param dniProfesor DNI del profesor.
     * @param codigoMateria Código de la materia.
     * @param idPeriodo ID del periodo.
     * @return true si se eliminó.
     */
    boolean deleteById(long dniProfesor, long codigoMateria, long idPeriodo);
}