package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Profesor;

import java.util.List;
import java.util.Optional;

public class ActiveJDBC_ProfesorRepository implements ProfesorRepository {
    @Override
    public Optional<Profesor> findByDni(long dni) {
        // En ActiveJDBC, el 'dni' es la PK de la tabla Profesor
        return Optional.ofNullable(Profesor.findById(dni));
    }

    @Override
    public void save(Profesor profesor) {
        profesor.saveIt();
    }

    @Override
    public void delete(Profesor profesor) {
        profesor.delete();
    }

    @Override
    public List<Profesor> findAll() {
        // Versión simple: Devuelve todos los profesores sin un orden específico.
        // La versión con JOIN es más útil para la UI, pero esta es más directa.
        return Profesor.findAll();
    }
}