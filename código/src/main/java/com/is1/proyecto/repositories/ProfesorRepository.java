package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Profesor;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de persistencia para la entidad Profesor.
 * Sigue el patrón Repository para abstraer y encapsular el comportamiento de 
 * almacenamiento, recuperación y búsqueda de profesores.
 */
public interface ProfesorRepository {
    
    /**
     * Recupera todos los profesores almacenados.
     * @return Lista de todos los profesores
     */
    List<Profesor> findAll();

    /**
     * Busca un profesor por su número de legajo.
     * @param legajo Número de legajo del profesor a buscar
     * @return Optional conteniendo el profesor si existe, vacío si no
     */
    Optional<Profesor> findByLegajo(Integer legajo);

    /**
     * Busca profesores por su apellido.
     * @param apellido Apellido o parte del apellido a buscar
     * @return Lista de profesores que coinciden con el criterio
     */
    List<Profesor> findByApellido(String apellido);

    /**
     * Guarda un nuevo profesor.
     * @param profesor Profesor a guardar
     * @throws IllegalArgumentException si ya existe un profesor con el mismo legajo
     */
    void save(Profesor profesor);

    /**
     * Actualiza los datos de un profesor existente.
     * @param profesor Profesor con los datos actualizados
     * @throws IllegalArgumentException si el profesor no existe
     */
    void update(Profesor profesor);

    /**
     * Elimina un profesor por su número de legajo.
     * @param legajo Número de legajo del profesor a eliminar
     * @return true si se eliminó el profesor, false si no existía
     */
    boolean delete(Integer legajo);

    /**
     * Verifica si existe un profesor con el legajo especificado.
     * @param legajo Número de legajo a verificar
     * @return true si existe, false si no
     */
    boolean exists(Integer legajo);

    /**
     * Verifica si existe un profesor con el DNI especificado.
     * @param dni DNI a verificar
     * @return true si existe, false si no
     */
    boolean existsByDni(String dni);
}