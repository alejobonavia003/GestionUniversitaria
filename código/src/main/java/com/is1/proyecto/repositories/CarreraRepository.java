package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Carrera;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz (Contrato) para las operaciones de la entidad Carrera.
 * Define las acciones que el CarreraService puede realizar.
 */
public interface CarreraRepository {

    /**
     * Busca todas las carreras en la base de datos.
     * @return Una lista de todas las carreras.
     */
    List<Carrera> findAll();

    /**
     * Busca una carrera por su Clave Primaria (cod_carrera).
     * @param id El código de la carrera.
     * @return Un Optional que contiene la Carrera si se encuentra, o vacío si no.
     */
    Optional<Carrera> findById(int id);

    /**
     * Guarda una entidad Carrera (ya sea nueva o actualizada) en la DB.
     * @param carrera El objeto Carrera a guardar.
     */
    void save(Carrera carrera);

    /**
     * Elimina una entidad Carrera de la DB.
     * @param carrera El objeto Carrera a eliminar.
     */
    void delete(Carrera carrera);
    
    // Aquí podríamos agregar métodos más complejos, como:
    // List<Carrera> findByDuracion(int anios);
}