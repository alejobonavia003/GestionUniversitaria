package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Cursa;
import java.util.List;

/**
 * Interfaz para el repositorio de la entidad Cursa (Inscripción a Materia).
 */
public interface CursaRepository {

    /**
     * Busca todas las inscripciones (alumnos) de una materia específica.
     * @param codigoMateria El código de la materia.
     * @return Lista de inscripciones (Cursa).
     */
    List<Cursa> findByMateriaId(long codigoMateria);

    /**
     * Busca todas las inscripciones de un estudiante.
     * @param dniEstudiante El DNI del estudiante.
     * @return Lista de inscripciones (Cursa).
     */
    List<Cursa> findByEstudianteDni(long dniEstudiante);
    
    /**
     * Guarda (crea) una inscripción.
     * @param inscripcion La entidad Cursa.
     */
    void save(Cursa inscripcion);
}