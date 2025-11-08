package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.CompositePK;

/**
 * Mapea la tabla 'Dicta' o Asignación Docente.
 * Es una tabla de unión con atributos (cargo, participacion) y FKs a Profesor, Materia y Periodo.
 * Clave primaria compuesta por (dni_prof, codigo_materia, id_periodo).
 */
@Table("Dicta")
@CompositePK({"dni_prof", "codigo_materia", "id_periodo"})
@BelongsToParents({
    // Pertenece a Profesor
    @BelongsTo(parent = Profesor.class, foreignKeyName = "dni_prof"),
    // Pertenece a Materia
    @BelongsTo(parent = Materia.class, foreignKeyName = "codigo_materia"),
    // Pertenece a Periodo
    @BelongsTo(parent = Periodo.class, foreignKeyName = "id_periodo")
})
public class Dicta extends Model {
    
    // --- Getters & Setters para Atributos de la Relación ---
    
    /**
     * Obtiene el Cargo del docente en la asignación (ej: Titular, Adjunto).
     */
    public String getCargo() {
        return getString("cargo");
    }

    /**
     * Obtiene la Participación del docente (ej: Responsable, Colaborador).
     */
    public String getParticipacion() {
        return getString("participacion");
    }

    public void setCargo(String cargo) {
        set("cargo", cargo);
    }

    public void setParticipacion(String participacion) {
        set("participacion", participacion);
    }
    
    // --- Métodos de Conveniencia y FKs ---

    public int getDniProfesor() {
        return getInteger("dni_prof");
    }

    public int getCodigoMateria() {
        return getInteger("codigo_materia");
    }
    
    public int getIdPeriodo() {
        return getInteger("id_periodo");
    }
    
    /**
     * Obtiene el objeto Profesor asociado.
     */
    public Profesor getProfesor() {
        return parent(Profesor.class);
    }

    /**
     * Obtiene el objeto Materia asociado.
     */
    public Materia getMateria() {
        return parent(Materia.class);
    }

    /**
     * Obtiene el objeto Periodo asociado.
     */
    public Periodo getPeriodo() {
        return parent(Periodo.class);
    }
}