package com.is1.proyecto.models;

/**
 * Clase de dominio que representa un Profesor en el sistema.
 * Esta clase es un objeto de dominio puro y no tiene dependencias
 * con la capa de persistencia.
 */
public class Profesor {
    private Integer legajo;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;

    /**
     * Constructor que inicializa un nuevo Profesor con todos sus atributos.
     * 
     * @param legajo   Número de legajo único del profesor
     * @param nombre   Nombre del profesor
     * @param apellido Apellido del profesor
     * @param dni      DNI del profesor
     * @param email    Correo electrónico del profesor
     * @param telefono Número de teléfono del profesor
     */
    public Profesor(Integer legajo, String nombre, String apellido, String dni, String email, String telefono) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        validar();
    }

    /**
     * Valida que los datos del profesor sean correctos.
     * @throws IllegalArgumentException si algún dato es inválido
     */
    private void validar() {
        if (legajo == null || legajo <= 0) {
            throw new IllegalArgumentException("El legajo debe ser un número positivo");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe tener 8 dígitos");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El email no es válido");
        }
    }

    // Getters y setters con validación

    public Integer getLegajo() {
        return legajo;
    }

    public void setLegajo(Integer legajo) {
        if (legajo == null || legajo <= 0) {
            throw new IllegalArgumentException("El legajo debe ser un número positivo");
        }
        this.legajo = legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre.trim();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        this.apellido = apellido.trim();
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe tener 8 dígitos");
        }
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El email no es válido");
        }
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el nombre completo del profesor.
     * @return String con el nombre y apellido concatenados
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}