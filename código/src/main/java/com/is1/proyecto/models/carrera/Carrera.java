package com.is1.proyecto.models.carrera;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("Carrera")
public class Carrera extends Model {

	public Integer getCodigo() {
		return getInteger("cod_carrera");
	}

	public void setCodigo(Integer codigo) {
		set("cod_carrera", codigo);
	}

	public String getNombre() {
		return getString("nombre");
	}

	public void setNombre(String nombre) {
		set("nombre", nombre);
	}

	public Integer getDuracion() {
		return getInteger("duracion");
	}

	public void setDuracion(Integer duracion) {
		set("duracion", duracion);
	}

	public static Carrera findByCodigo(Integer codigo) {
		return findFirst("cod_carrera = ?", codigo);
	}

	public java.util.List<PlanDeEstudio> getPlanes() {
		return PlanDeEstudio.find("cod_carrera = ?", getCodigo());
	}
}