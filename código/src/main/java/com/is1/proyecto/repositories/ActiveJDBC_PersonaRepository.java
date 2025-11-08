package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Persona;
import java.util.Optional;

public class ActiveJDBC_PersonaRepository implements PersonaRepository {
    @Override
    public Optional<Persona> findByDni(long dni) {
        return Optional.ofNullable(Persona.findById(dni));
    }
    
    @Override
    public void save(Persona persona) {
        persona.saveIt();
    }

    @Override
    public void delete(Persona persona) {
        persona.delete();
    }
}