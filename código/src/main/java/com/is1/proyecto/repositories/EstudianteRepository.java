package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Estudiante;
import java.util.Optional;

public interface EstudianteRepository {
    Optional<Estudiante> findByDni(long dni);
    void save(Estudiante estudiante);
    void delete(Estudiante estudiante);
}