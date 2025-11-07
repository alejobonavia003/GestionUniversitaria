package com.is1.proyecto.models.persona;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("Dicta")
public class Dicta extends Model {

	// DB columns: dni_prof, codigo_materia
	public Integer getDniProf() {
		return getInteger("dni_prof");
	}

	public void setDniProf(Integer dni) {
		set("dni_prof", dni);
	}

	public Integer getCodigoMateria() {
		return getInteger("codigo_materia");
	}

	public void setCodigoMateria(Integer codigo) {
		set("codigo_materia", codigo);
	}

	public Profesor getProfesor() {
		Integer dni = getDniProf();
		if (dni == null) return null;
		return Profesor.findFirst("dni = ?", dni);
	}

	public com.is1.proyecto.models.carrera.Materia getMateria() {
		Integer codigo = getCodigoMateria();
		if (codigo == null) return null;
		return com.is1.proyecto.models.carrera.Materia.findFirst("codigo = ?", codigo);
	}
}
