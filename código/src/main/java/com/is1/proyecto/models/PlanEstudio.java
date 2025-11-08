package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.HasMany;
import java.util.List;

/**
 * Mapea la tabla 'PlanEstudio'.
 * Representa una versión específica del plan de estudios de una Carrera.
 */
@Table("PlanEstudio")
// 1. Relación BelongsTo: Un PlanEstudio pertenece a una Carrera (usando cod_carrera como FK)
@BelongsTo(parent = Carrera.class, foreignKeyName = "cod_carrera")
// 2. Relación HasMany: Un PlanEstudio tiene muchas Materias (usando id_plan como FK en Materia)
@HasMany(child = Materia.class, foreignKeyName = "id_plan")
public class PlanEstudio extends Model {

    // --- Getters ---
    
    // El 'id' es la PK, ActiveJDBC lo infiere.
    public Integer getId() {
        return getInteger("id");
    }

    public int getAnioPlan() {
        return getInteger("anio_plan");
    }

    public int getVersion() {
        return getInteger("version");
    }

    public int getCodigoCarrera() {
        return getInteger("cod_carrera");
    }

    // --- Setters ---

    // No se necesita setId(), ya que es autoincremental en la DB
    
    public void setAnioPlan(int anioPlan) {
        set("anio_plan", anioPlan);
    }

    public void setVersion(int version) {
        set("version", version);
    }
    
    // setCodigoCarrera solo se usa si se asocia manualmente
    public void setCodigoCarrera(int codCarrera) {
        set("cod_carrera", codCarrera);
    }

    // --- Relaciones ---
    
    /**
     * Obtiene la Carrera a la que pertenece este Plan de Estudio.
     * @return Objeto Carrera.
     */
    public Carrera getCarrera() {
        return parent(Carrera.class);
    }

    /**
     * Obtiene todas las Materias que forman parte de este Plan de Estudio.
     * @return Lista de objetos Materia.
     */
    public List<Materia> getMaterias() {
        return getAll(Materia.class);
    }
}