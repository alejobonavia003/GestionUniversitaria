package com.is1.proyecto.dto;

/**
 * DTO (Data Transfer Object) para la entidad Carrera.
 * Esta clase se usa para transferir datos entre capas y hacia/desde la vista.
 */
public class CarreraDTO {
    private Integer codigo;
    private String nombre;
    private Integer duracion;

    // Constructor
    public CarreraDTO() {}

    public CarreraDTO(Integer codigo, String nombre, Integer duracion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.duracion = duracion;
    }

    // Getters y setters
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }
}