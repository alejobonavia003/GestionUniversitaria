package com.is1.proyecto.models.carrera;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("Vigente")
public class Vigente extends Model {

	public Integer getCodCarrera() {
		return getInteger("cod_carrera");
	}

	public void setCodCarrera(Integer cod) {
		set("cod_carrera", cod);
	}

	public Integer getIdPlan() {
		return getInteger("id_plan");
	}

	public void setIdPlan(Integer id) {
		set("id_plan", id);
	}

	public Carrera getCarrera() {
		Integer cod = getCodCarrera();
		if (cod == null) return null;
		return Carrera.findByCodigo(cod);
	}

	public PlanDeEstudio getPlan() {
		Integer id = getIdPlan();
		if (id == null) return null;
		return PlanDeEstudio.findById(id);
	}
}