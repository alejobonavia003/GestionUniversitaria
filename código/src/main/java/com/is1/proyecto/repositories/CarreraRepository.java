package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Carrera;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de persistencia para la entidad Carrera.
 */
public interface CarreraRepository {
    List<Carrera> findAll();
    Optional<Carrera> findById(Integer codigo);
    void save(Carrera carrera);
    void update(Carrera carrera);
    void delete(Integer codigo);
    boolean exists(Integer codigo);
}