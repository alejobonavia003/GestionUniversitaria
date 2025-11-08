package com.is1.proyecto.models;

import java.util.Optional;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.CompositePK;

/**
 * Mapea la tabla 'Rindio' (Nota_Final en UML).
 * Es una tabla de unión con atributos (nota, condicion) entre Estudiante y Materia.
 * La clave primaria es compuesta por (dni, codigo_materia).
 */
@Table("Rindio") // O "NotaFinal", la que uses
@CompositePK({"dni", "codigo_materia"}) 
@BelongsToParents({ // <-- ¡LA SOLUCIÓN!
    @BelongsTo(parent = Estudiante.class, foreignKeyName = "dni"),
    @BelongsTo(parent = Materia.class, foreignKeyName = "codigo_materia")
})
public class NotaFinal extends Model {

    // --- Relaciones de Pertenencia ---
    

    
    // --- Getters & Setters para Atributos de la Relación ---
    
    public int getNota() {
        return getInteger("nota");
    }

    public String getCondicion() {
        return getString("condicion");
    }

    public void setNota(int nota) {
        set("nota", nota);
    }

    public void setCondicion(String condicion) {
        set("condicion", condicion);
    }
    
    // --- Métodos de Conveniencia y FKs ---

    public int getDniEstudiante() {
        return getInteger("dni");
    }

    public int getCodigoMateria() {
        return getInteger("codigo_materia");
    }
    
    /**
     * Obtiene el objeto Estudiante asociado a esta nota.
     */
    public Estudiante getEstudiante() {
        return parent(Estudiante.class);
    }

    /**
     * Obtiene el objeto Materia asociada a esta nota.
     */
    public Materia getMateria() {
        return parent(Materia.class);
    }

}