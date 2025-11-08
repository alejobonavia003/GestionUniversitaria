package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * Mapea la tabla 'Periodo'. 
 * Representa un ciclo lectivo concreto (ej. C1-2025, Anual-2024).
 * Asumimos una tabla simple para el concepto de tiempo.
 */
@Table("Periodo")
// Asumimos que la tabla de asignación de profesores (Dicta) se vinculará a esta.
// @HasMany(other = Dicta.class, foreignKeyName = "id_periodo") // Se implementará después
public class Periodo extends Model {

    // --- Atributos de ejemplo para un Periodo concreto ---
    
    // Asumimos un ID autoincremental (ActiveJDBC lo maneja por defecto)
    
    // Descripción del período (ej: "Primer Cuatrimestre 2025")
    public String getDescripcion() {
        return getString("descripcion");
    }

    public void setDescripcion(String descripcion) {
        set("descripcion", descripcion);
    }
    
    // Tipo de período (ej: "Cuatrimestral", "Anual")
    public String getTipo() {
        return getString("tipo");
    }

    public void setTipo(String tipo) {
        set("tipo", tipo);
    }

    // El año (ej: 2025)
    public int getAnio() {
        return getInteger("anio");
    }
    
    public void setAnio(int anio) {
        set("anio", anio);
    }
    
    // --- Métodos de Conveniencia ---
    
    // Si la tabla Dicta se mapea, este método obtendrá las asignaciones.
    // public List<Dicta> getAsignaciones() {
    //     return getAll(Dicta.class);
    // }
}