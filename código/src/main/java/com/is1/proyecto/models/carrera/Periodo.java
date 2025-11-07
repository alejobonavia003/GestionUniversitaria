package com.is1.proyecto.models.carrera;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("Periodo")
public class Periodo extends Model {

    public Integer getAnio() {
        return getInteger("anio");
    }

    public void setAnio(Integer anio) {
        set("anio", anio);
    }

    public Integer getPeriodo() {
        return getInteger("periodo");
    }

    public void setPeriodo(Integer periodo) {
        set("periodo", periodo);
    }

    public String getDescripcion() {
        return getString("descripcion");
    }

    public void setDescripcion(String desc) {
        set("descripcion", desc);
    }
}
