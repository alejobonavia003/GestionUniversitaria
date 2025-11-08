package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Profesor;

import java.util.List;
import java.util.Optional;

public interface ProfesorRepository {
    Optional<Profesor> findByDni(long dni);
    void save(Profesor profesor);
    void delete(Profesor profesor);
    List<Profesor> findAll();
}