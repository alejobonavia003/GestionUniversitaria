package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Persona;
import java.util.Optional;

public interface PersonaRepository {
    Optional<Persona> findByDni(long dni);
    void save(Persona persona);
    void delete(Persona persona);
}