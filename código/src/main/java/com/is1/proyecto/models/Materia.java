package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Many2Many;
import java.util.List;

/**
 * Mapea la tabla 'Materia'.
 * Es la entidad central académica que maneja correlatividades, planes y docentes.
 */
@Table("Materia")
// 1. Relación N:1: Pertenece a un Plan de Estudio.
@BelongsTo(parent = PlanEstudio.class, foreignKeyName = "id_plan")
// 2. Relación N:M Recursiva (Correlativas): Una materia tiene muchas correlativas y es correlativa de muchas otras.
// Tabla de unión: Correlativa (codigo_materia, codigo_correlativa)
@Many2Many(other = Materia.class, join = "Correlativa", sourceFKName = "codigo_materia", targetFKName = "codigo_correlativa")
// 3. Relación N:M Profesores: Materia dictada por Profesores a través de la tabla 'Dicta'.
@Many2Many(other = Profesor.class,
            join = "Dicta",
            sourceFKName = "codigo_materia",
            targetFKName = "dni_prof") // Usamos dni_prof, que es la PK compartida en Profesor
public class Materia extends Model {

    
    // --- Getters & Setters ---

    public int getCodigo() {
        return getInteger("codigo");
    }

    public String getNombre() {
        return getString("nombre");
    }

    public void setNombre(String nombre) {
        set("nombre", nombre);
    }
    
    // Se usa setParent(getPlanEstudio()) de ActiveJDBC para configurar la FK.
    public PlanEstudio getPlanEstudio() {
        return parent(PlanEstudio.class);
    }

    // --- Relaciones: Métodos de Conveniencia ---

    
    /**
     * Obtiene la lista de Profesores que dictan esta Materia.
     * @return Lista de Profesor.
     */
    public List<Profesor> getProfesores() {
        return getAll(Profesor.class);
    }
    
    /**
     * Agrega una materia como correlativa requerida.
     * @param correlativa La materia requerida.
     */
    public void addCorrelativa(Materia correlativa) {
        add(correlativa);
    }

    // TODO: Las relaciones con Estudiante (Cursa/Rindio) se gestionarán mejor
    // creando las clases de Modelos intermedias 'Cursa' y 'Rindio' para 
    // manejar la Nota y la Condición (que son atributos de la relación).
    // Esto lo haremos más adelante.
}