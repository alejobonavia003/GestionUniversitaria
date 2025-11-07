package com.is1.proyecto.models.persona;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("Rindio")
public class Rindio extends Model {

	public Integer getDni() {
		return getInteger("dni");
	}

	public void setDni(Integer dni) {
		set("dni", dni);
	}

	public Integer getCodigoMateria() {
		return getInteger("codigo_materia");
	}

	public void setCodigoMateria(Integer codigo) {
		set("codigo_materia", codigo);
	}

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

	public Estudiante getEstudiante() {
		Integer dni = getDni();
		if (dni == null) return null;
		return Estudiante.findFirst("dni = ?", dni);
	}

	public com.is1.proyecto.models.carrera.Materia getMateria() {
		Integer codigo = getCodigoMateria();
		if (codigo == null) return null;
		return com.is1.proyecto.models.carrera.Materia.findFirst("codigo = ?", codigo);
	}
}