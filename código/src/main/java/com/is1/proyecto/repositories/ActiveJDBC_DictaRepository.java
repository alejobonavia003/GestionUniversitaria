package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Dicta;
import java.util.List;


/**
 * Implementación de DictaRepository usando ActiveJDBC.
 */
public class ActiveJDBC_DictaRepository implements DictaRepository {

    @Override
    public List<Dicta> findByMateriaId(long codigoMateria) {
        // Busca usando la clave foránea
        return Dicta.where("codigo_materia = ?", codigoMateria);
    }

    @Override
    public List<Dicta> findByProfesorDni(long dniProfesor) {
        // Busca usando la clave foránea
        return Dicta.where("dni_prof = ?", dniProfesor);
    }

    @Override
    public void save(Dicta asignacion) {
        asignacion.saveIt();
    }

    @Override
    public boolean deleteById(long dniProfesor, long codigoMateria, long idPeriodo) {
        // ActiveJDBC usa .delete() con la PK compuesta
        int count = Dicta.delete("dni_prof = ? AND codigo_materia = ? AND id_periodo = ?", 
                                 dniProfesor, codigoMateria, idPeriodo);
        return count > 0;
    }
}