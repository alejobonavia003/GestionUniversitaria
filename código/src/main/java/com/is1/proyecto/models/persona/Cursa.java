package com.is1.proyecto.models.persona;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import com.is1.proyecto.models.carrera.Materia;
import com.is1.proyecto.models.persona.Estudiante;

@Table("Cursa")
public class Cursa extends Model {

	// In DB the columns are: dni, codigo_materia
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

	public Estudiante getEstudiante() {
		Integer dni = getDni();
		if (dni == null) return null;
		return Estudiante.findFirst("dni = ?", dni);
	}

	public Materia getMateria() {
		Integer codigo = getCodigoMateria();
		if (codigo == null) return null;
		return Materia.findFirst("codigo = ?", codigo);
	}
}
