package com.is1.proyecto.models.persona;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("Nota_Final")
public class NotaFinal extends Model {

    public Integer getNota() {
        return getInteger("nota");
    }

    public void setNota(Integer nota) {
        set("nota", nota);
    }

    public String getCondicion() {
        return getString("condicion");
    }

    public void setCondicion(String condicion) {
        set("condicion", condicion);
    }

    public Integer getEstudianteDni() {
        return getInteger("estudiante_dni");
    }

    public void setEstudianteDni(Integer dni) {
        set("estudiante_dni", dni);
    }

    public Integer getMateriaCodigo() {
        return getInteger("materia_codigo");
    }

    public void setMateriaCodigo(Integer codigo) {
        set("materia_codigo", codigo);
    }
}
