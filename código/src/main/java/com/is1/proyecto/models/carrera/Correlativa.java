package com.is1.proyecto.models.carrera;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("Correlativa")
public class Correlativa extends Model {

	public Integer getCodigoMateria() {
		return getInteger("codigo_materia");
	}

	public void setCodigoMateria(Integer id) {
		set("codigo_materia", id);
	}

	public Integer getCodigoCorrelativa() {
		return getInteger("codigo_correlativa");
	}

	public void setCodigoCorrelativa(Integer id) {
		set("codigo_correlativa", id);
	}

	public Materia getMateria() {
		Integer codigo = getCodigoMateria();
		if (codigo == null) return null;
		return Materia.findFirst("codigo = ?", codigo);
	}
}