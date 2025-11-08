package com.is1.proyecto.repositories;

import com.is1.proyecto.models.NotaFinal;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de NotaFinalRepository usando ActiveJDBC.
 */
public class ActiveJDBC_NotaFinalRepository implements NotaFinalRepository {

@Override
    public Optional<NotaFinal> findById(long dniEstudiante, long codigoMateria) {
        
        // ¡LA SOLUCIÓN MANUAL!
        // En lugar de usar el "mágico" NotaFinal.findById(dni, cod),
        // usamos el método base .findFirst() con una consulta WHERE explícita
        // que busca por la clave primaria compuesta.
        
        NotaFinal nota = NotaFinal.findFirst("dni = ? AND codigo_materia = ?", 
                                              dniEstudiante, 
                                              codigoMateria);
                                              
        return Optional.ofNullable(nota);
    }

    @Override
    public List<NotaFinal> findByEstudianteDni(long dniEstudiante) {
        return NotaFinal.where("dni = ?", dniEstudiante);
    }

    @Override
    public List<NotaFinal> findByMateriaId(long codigoMateria) {
        return NotaFinal.where("codigo_materia = ?", codigoMateria);
    }

    @Override
    public void save(NotaFinal nota) {
        // saveIt() maneja la inserción o actualización basada en la PK compuesta
        nota.saveIt();
    }

   
}