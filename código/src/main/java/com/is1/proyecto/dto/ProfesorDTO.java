package com.is1.proyecto.dto;

/**
 * Data Transfer Object (DTO) para la entidad Profesor.
 * Esta clase se utiliza para transferir datos del profesor entre las diferentes capas
 * de la aplicación y hacia/desde la capa de presentación.
 */
public class ProfesorDTO {
    private Integer legajo;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;

    /**
     * Constructor por defecto necesario para la serialización/deserialización.
     */
    public ProfesorDTO() {}

    /**
     * Constructor que inicializa todos los campos del profesor.
     * 
     * @param legajo   Número de legajo único del profesor
     * @param nombre   Nombre del profesor
     * @param apellido Apellido del profesor
     * @param dni      DNI del profesor
     * @param email    Correo electrónico del profesor
     * @param telefono Número de teléfono del profesor
     */
    public ProfesorDTO(Integer legajo, String nombre, String apellido, String dni, String email, String telefono) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
    }

    // Getters y Setters con documentación JavaDoc

    /**
     * Obtiene el número de legajo del profesor.
     * @return Número de legajo
     */
    public Integer getLegajo() {
        return legajo;
    }

    /**
     * Establece el número de legajo del profesor.
     * @param legajo Número de legajo a establecer
     */
    public void setLegajo(Integer legajo) {
        this.legajo = legajo;
    }

    /**
     * Obtiene el nombre del profesor.
     * @return Nombre del profesor
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del profesor.
     * @param nombre Nombre a establecer
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del profesor.
     * @return Apellido del profesor
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del profesor.
     * @param apellido Apellido a establecer
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el DNI del profesor.
     * @return DNI del profesor
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del profesor.
     * @param dni DNI a establecer
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el email del profesor.
     * @return Email del profesor
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email del profesor.
     * @param email Email a establecer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el teléfono del profesor.
     * @return Teléfono del profesor
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono del profesor.
     * @param telefono Teléfono a establecer
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}