package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Periodo;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de PeriodoRepository usando ActiveJDBC.
 */
public class ActiveJDBC_PeriodoRepository implements PeriodoRepository {

    @Override
    public Optional<Periodo> findById(long id) {
        return Optional.ofNullable(Periodo.findById(id));
    }

    @Override
    public List<Periodo> findAll() {
        // Ordena por año descendente, luego por descripción (ej. "Segundo Cuatrimestre" antes de "Primero")
        return Periodo.findAll().orderBy("anio DESC, descripcion DESC");
    }

    @Override
    public void save(Periodo periodo) {
        periodo.saveIt();
    }

    @Override
    public boolean deleteById(long id) {
        // (Aquí faltaría la validación de negocio: no borrar si tiene asignaciones 'Dicta')
        int count = Periodo.delete("id = ?", id);
        return count > 0;
    }
}