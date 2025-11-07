package com.is1.proyecto.models;

/**
 * Modelo de dominio que representa una Carrera en el sistema.
 * Esta clase es un POJO (Plain Old Java Object) que representa el concepto de negocio
 * sin depender de la persistencia.
 */
public class Carrera {
    private Integer codigo;
    private String nombre;
    private Integer duracion;

    // Constructor
    public Carrera(Integer codigo, String nombre, Integer duracion) {
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