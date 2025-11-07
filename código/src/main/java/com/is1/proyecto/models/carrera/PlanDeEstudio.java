package com.is1.proyecto.models.carrera;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("PlanEstudio")
public class PlanDeEstudio extends Model {

	public Integer getId() {
		return getInteger("id");
	}

	public void setId(Integer id) {
		set("id", id);
	}

	public Integer getAnioPlan() {
		return getInteger("anio_plan");
	}

	public void setAnioPlan(Integer anio) {
		set("anio_plan", anio);
	}

	public Integer getVersion() {
		return getInteger("version");
	}

	public void setVersion(Integer version) {
		set("version", version);
	}

	public Integer getCodCarrera() {
		return getInteger("cod_carrera");
	}

	public void setCodCarrera(Integer cod) {
		set("cod_carrera", cod);
	}

	public static PlanDeEstudio findById(Integer id) {
		return findFirst("id = ?", id);
	}

	public Carrera getCarrera() {
		Integer cod = getCodCarrera();
		if (cod == null) return null;
		return Carrera.findByCodigo(cod);
	}
}