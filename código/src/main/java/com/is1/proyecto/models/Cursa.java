package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents; // Usamos el contenedor
import org.javalite.activejdbc.annotations.CompositePK;

/**
 * Mapea la tabla 'Cursa'.
 * Es una tabla de unión (N:M) entre Estudiante y Materia,
 * que representa la inscripción de un alumno a una materia.
 */
@Table("Cursa")
@CompositePK({"dni", "codigo_materia"}) // Clave primaria compuesta
@BelongsToParents({
    // Pertenece a un Estudiante
    @BelongsTo(parent = Estudiante.class, foreignKeyName = "dni"),
    // Pertenece a una Materia
    @BelongsTo(parent = Materia.class, foreignKeyName = "codigo_materia")
})
public class Cursa extends Model {

    // --- Métodos de Conveniencia y FKs ---

    public int getDniEstudiante() {
        return getInteger("dni");
    }

    public int getCodigoMateria() {
        return getInteger("codigo_materia");
    }
    
    /**
     * Obtiene el objeto Estudiante asociado a esta inscripción.
     */
    public Estudiante getEstudiante() {
        return parent(Estudiante.class);
    }

    /**
     * Obtiene el objeto Materia asociada a esta inscripción.
     */
    public Materia getMateria() {
        return parent(Materia.class);
    }
}