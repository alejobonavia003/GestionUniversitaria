package com.is1.proyecto.models.persona;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("Estudiante")
public class Estudiante extends PersonaAbs {

    public String getLegajo() {
        return getString("legajo");
    }

    public void setLegajo(String legajo) {
        set("legajo", legajo);
    }

    public PersonaConcreta getPersona() {
        Integer dni = getDni();
        if (dni == null) return null;
        return PersonaConcreta.findFirst("dni = ?", dni);
    }
}
//