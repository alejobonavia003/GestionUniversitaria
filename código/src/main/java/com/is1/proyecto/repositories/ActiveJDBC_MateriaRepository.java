package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Materia;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de MateriaRepository usando ActiveJDBC.
 */
public class ActiveJDBC_MateriaRepository implements MateriaRepository {

    @Override
    public Optional<Materia> findById(long id) {
        // En Materia, la PK es 'codigo', no 'id'.
        Materia materia = Materia.findById(id); // ActiveJDBC usa la PK definida en el modelo
        return Optional.ofNullable(materia);
    }

    @Override
    public List<Materia> findByPlanId(long planId) {
        // Busca usando la clave foránea 'id_plan'
        return Materia.where("id_plan = ?", planId).orderBy("nombre");
    }

    @Override
    public List<Materia> findAll() {
        // Devuelve todas las materias, ordenadas alfabéticamente por nombre para facilitar su uso en listas.
        return Materia.findAll().orderBy("nombre");
    }

    @Override
    public void save(Materia materia) {
        materia.saveIt();
    }

    @Override
    public boolean deleteById(long id) {
        // Devuelve el número de filas afectadas
        int count = Materia.delete("codigo = ?", id);
        return count > 0;
    }
}