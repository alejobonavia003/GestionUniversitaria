package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Estudiante;
import java.util.Optional;

public class ActiveJDBC_EstudianteRepository implements EstudianteRepository {
    @Override
    public Optional<Estudiante> findByDni(long dni) {
        // En ActiveJDBC, el 'dni' es la PK de la tabla Estudiante
        return Optional.ofNullable(Estudiante.findById(dni));
    }

    @Override
    public void save(Estudiante estudiante) {
        estudiante.saveIt();
    }

    @Override
    public void delete(Estudiante estudiante) {
        estudiante.delete();
    }
}