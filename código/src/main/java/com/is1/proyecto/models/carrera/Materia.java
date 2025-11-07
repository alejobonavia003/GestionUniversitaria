package com.is1.proyecto.models.carrera;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import java.util.List;
import java.util.ArrayList;

@Table("Materia")
public class Materia extends Model {
    
    public Integer getCodigo() {
        return getInteger("codigo");
    }

    public void setCodigo(Integer codigo) {
        set("codigo", codigo);
    }
    
    public String getNombre() {
        return getString("nombre");
    }

    public void setNombre(String nombre) {
        set("nombre", nombre);
    }

    public Integer getIdPlan() {
        return getInteger("id_plan");
    }

    public void setIdPlan(Integer id_plan) {
        set("id_plan", id_plan);
    }

    public PlanDeEstudio getPlan() {
        Integer idPlan = getIdPlan();
        if (idPlan == null) return null;
        return PlanDeEstudio.findById(idPlan);
    }

    public void addCorrelativa(String codigoCorrelativa) {
        if (!codigoCorrelativa.equals(getString("codigo_materia"))) { // Evitar auto-correlativas
            Correlativa.createIt(
                "codigo_materia", getString("codigo_materia"),
                "codigo_correlativa", codigoCorrelativa
            );
        }
    }

    public List<String> getCorrelativas() {
        List<Correlativa> correlativas = Correlativa.where("codigo_materia = ?", getString("codigo_materia"));
        List<String> codigosCorrelativas = new ArrayList<>();
        for (Correlativa c : correlativas) {
            codigosCorrelativas.add(c.getString("codigo_correlativa"));
        }
        return codigosCorrelativas;
    }

    public void removeAllCorrelativas() {
        Correlativa.delete("codigo_materia = ?", getString("codigo_materia"));
    }
}
//