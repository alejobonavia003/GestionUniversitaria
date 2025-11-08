package com.is1.proyecto.repositories;

import com.is1.proyecto.models.PlanEstudio;
import org.javalite.activejdbc.LazyList;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de PlanEstudioRepository usando ActiveJDBC.
 */
public class ActiveJDBC_PlanEstudioRepository implements PlanEstudioRepository {

    @Override
    public Optional<PlanEstudio> findById(long id) {
        // .findById() de ActiveJDBC devuelve el objeto o null
        PlanEstudio plan = PlanEstudio.findById(id);
        return Optional.ofNullable(plan);
    }

    @Override
    public List<PlanEstudio> findByCarreraId(int codCarrera) {
        // Usamos .where() para buscar por la clave foránea
        // .orderBy("anio_plan DESC") para mostrar los más nuevos primero
        return PlanEstudio.where("cod_carrera = ?", codCarrera).orderBy("anio_plan DESC");
    }

    @Override
    public void save(PlanEstudio plan) {
        // .saveIt() de ActiveJDBC maneja tanto la inserción como la actualización
        // Devuelve true/false si la validación (si la hubiera) pasa.
        plan.saveIt();
    }

    @Override
    public boolean deleteById(long id) {
        // .delete() de ActiveJDBC elimina el registro por la PK
        // Devuelve el número de filas afectadas
        int count = PlanEstudio.delete("id = ?", id);
        return count > 0;
    }
}