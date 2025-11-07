package com.is1.proyecto.models.persona;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("Profesor")
public class Profesor extends Model {
    
    public String getIdProfesor() {
        return getString("id_doc");
    }

    public Profesor() {
        // Constructor por defecto
    }
    
    public static boolean existsByDni(Integer dni) {
        return findFirst("dni = ?", dni) != null;
    }
    
    public static Profesor findByDni(Integer dni) {
        return findFirst("dni = ?", dni);
    }

    public void setDni(Integer dni) {
        set("dni", dni);
    }

    public PersonaConcreta getPersona() {
        Integer dni = getInteger("dni");
        if (dni == null) return null;
        return PersonaConcreta.findFirst("dni = ?", dni);
    }


}////