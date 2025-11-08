package com.is1.proyecto.models;

import org.javalite.activejdbc.Model; // Ahora extiende Model directamente.
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.BelongsTo;

/**
 * Mapea la tabla 'Estudiante'.
 * Usa ActiveJDBC para la persistencia. La relación 1:1 con Persona se gestiona
 * a través de la clave primaria compartida 'dni' y la anotación @BelongsTo.
 */
@Table("Estudiante")
// La relación BelongsTo mapea la clave externa dni de Estudiante a la clave primaria dni de Persona.
@BelongsTo(parent = Persona.class, foreignKeyName = "dni") 
public class Estudiante extends Model { 
    
    // --- Atributos específicos de Estudiante ---
    
    public String getLegajo() {
        return getString("legajo");
    }

    public void setLegajo(String legajo) {
        set("legajo", legajo);
    }
    
    // El DNI se accede como cualquier campo de la tabla Estudiante.
    public int getDni() {
        return getInteger("dni");
    }
    
    // TODO: Falta el atributo 'estado' (evaluado/ingresante) del UML. 
    // Lo manejaremos con un Enum o String cuando definamos las enumeraciones.
    
    // --- Métodos de Conveniencia y Relación ---
    
    /**
     * Obtiene el objeto Persona asociado para acceder a nombre, apellido, email, etc.
     * @return El objeto Persona si existe, o null.
     */
    public Persona getDatosPersona() {
        // ActiveJDBC usa 'parent' para navegar la relación BelongsTo
        return parent(Persona.class);
    }
    
    /**
     * Método de conveniencia para obtener el nombre completo.
     */
    public String getNombreCompleto() {
        Persona p = getDatosPersona();
        return p != null ? p.getNombreCompleto() : "Datos de Persona no encontrados";
    }
}