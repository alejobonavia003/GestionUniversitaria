package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * Clase base que mapea la tabla 'Persona'.
 * Contiene los datos comunes de todos los individuos en el sistema.
 */
@Table("Persona") 
public class Persona extends Model {

    // --- Getters ---
    
    public int getDni() {
        // El DNI es la clave primaria compartida.
        // ActiveJDBC usa getInteger para campos INTEGER.
        return getInteger("dni"); 
    }

    public String getNombre() {
        return getString("nombre");
    }

    public String getApellido() {
        return getString("apellido");
    }

    public String getEmail() {
        return getString("email");
    }
    
    public String getTelefono() {
        return getString("telefono");
    }
    
    public String getDireccion() {
        return getString("direccion");
    }

    // --- Setters ---

    public void setDni(int dni) {
        set("dni", dni);
    }
    
    public void setNombre(String nombre) {
        set("nombre", nombre);
    }

    public void setApellido(String apellido) {
        set("apellido", apellido);
    }

    public void setEmail(String email) {
        set("email", email);
    }
    
    public void setTelefono(String telefono) {
        set("telefono", telefono);
    }
    
    public void setDireccion(String direccion) {
        set("direccion", direccion);
    }
    
    /**
     * Devuelve el nombre completo (Apellido, Nombre).
     * @return String
     */
    public String getNombreCompleto() {
        return getApellido() + ", " + getNombre();
    }
}