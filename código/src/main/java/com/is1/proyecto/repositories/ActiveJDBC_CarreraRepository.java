package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Carrera;
import org.javalite.activejdbc.LazyList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de CarreraRepository usando ActiveJDBC.
 * Traduce los métodos del contrato a llamadas de ActiveJDBC.
 */
public class ActiveJDBC_CarreraRepository implements CarreraRepository {

    @Override
    public List<Carrera> findAll() {
        // .findAll() de ActiveJDBC devuelve una LazyList, 
        // pero podemos devolverla como List gracias a la herencia.
        return Carrera.findAll();
    }

    @Override
    public Optional<Carrera> findById(int id) {
        // .findById() de ActiveJDBC devuelve la entidad o null.
        // Lo envolvemos en un Optional para cumplir con el contrato.
        Carrera carrera = Carrera.findById(id);
        return Optional.ofNullable(carrera);
    }

    @Override
    public void save(Carrera carrera) {
        // .saveIt() de ActiveJDBC maneja tanto la creación (INSERT) 
        // como la actualización (UPDATE).
        carrera.saveIt();
    }

    @Override
    public void delete(Carrera carrera) {
        // .delete() de ActiveJDBC elimina la fila de la DB.
        carrera.delete();
    }
}