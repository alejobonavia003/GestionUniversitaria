package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Cursa;
import java.util.List;

/**
 * Implementación de CursaRepository usando ActiveJDBC.
 */
public class ActiveJDBC_CursaRepository implements CursaRepository {

    @Override
    public List<Cursa> findByMateriaId(long codigoMateria) {
        return Cursa.where("codigo_materia = ?", codigoMateria);
    }

    @Override
    public List<Cursa> findByEstudianteDni(long dniEstudiante) {
        return Cursa.where("dni = ?", dniEstudiante);
    }
    
    @Override
    public void save(Cursa inscripcion) {
        // Asume que la PK es compuesta (dni, codigo_materia)
        inscripcion.saveIt();
    }
}