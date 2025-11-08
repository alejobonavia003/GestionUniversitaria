package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.HasMany;
import java.util.List;

/**
 * Mapea la tabla 'Carrera'.
 * Es el punto de partida de la estructura académica (tiene muchos Planes de Estudio).
 */
@Table("Carrera")
// Una Carrera tiene muchos Planes de Estudio (relación 1:N)
@IdName("cod_carrera")
@HasMany(child = PlanEstudio.class, foreignKeyName = "cod_carrera")
public class Carrera extends Model {

    // --- Getters ---
    
    // El cod_carrera es la PK, ActiveJDBC lo infiere, pero es bueno definir el getter.
    public int getCodigoCarrera() {
        return getInteger("cod_carrera");
    }

    public String getNombre() {
        return getString("nombre");
    }

    public int getDuracion() {
        return getInteger("duracion");
    }

    // --- Setters ---

    public void setCodigoCarrera(int codigo) {
        set("cod_carrera", codigo);
    }
    
    public void setNombre(String nombre) {
        set("nombre", nombre);
    }

    public void setDuracion(int duracion) {
        set("duracion", duracion);
    }
    
    // --- Relaciones ---
    
    /**
     * Obtiene todos los Planes de Estudio asociados a esta Carrera.
     * @return Lista de objetos PlanEstudio.
     */
    public List<PlanEstudio> getPlanesDeEstudio() {
        // ActiveJDBC usa findAll() sobre la relación HasMany definida
        return getAll(PlanEstudio.class); 
    }
    
    /**
     * Obtiene el Plan de Estudio Vigente (asumiendo que la tabla Vigente indica el actual).
     * Esta implementación requiere un Join o una consulta manual a la tabla 'Vigente'.
     * Por simplicidad de ActiveJDBC, lo dejamos como una nota a refactorizar.
     * Por ahora, devuelve el PlanEstudio con la versión más alta (último creado).
     * * NOTA: La lógica real debe consultar la tabla de JOIN 'Vigente'.
     */
    public PlanEstudio getPlanVigente() {
        // En una implementación real de ActiveJDBC, harías:
        // return PlanEstudio.findFirst("cod_carrera = ? AND id IN (SELECT id_plan FROM Vigente WHERE cod_carrera = ?)", 
        //                             getCodigoCarrera(), getCodigoCarrera());

        // Para simplificar, devolvemos el último plan:
        return PlanEstudio.findFirst("cod_carrera = ? order by version desc", getCodigoCarrera());
    }
}