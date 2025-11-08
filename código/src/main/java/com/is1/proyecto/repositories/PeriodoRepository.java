package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Periodo;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el repositorio de la entidad Periodo.
 */
public interface PeriodoRepository {
    Optional<Periodo> findById(long id);
    List<Periodo> findAll();
    void save(Periodo periodo);
    boolean deleteById(long id);
}