package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Profesor;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del repositorio de Profesor usando ActiveJDBC.
 * Esta clase maneja la persistencia de los profesores y la conversión
 * entre las entidades de la base de datos y los objetos de dominio.
 */
@Table("Profesor")
public class ProfesorRepositoryImpl implements ProfesorRepository {
    
    /**
     * Clase interna que representa la entidad Profesor en la base de datos.
     * Extiende de Model de ActiveJDBC para la persistencia.
     */
    private static class ProfesorEntity extends Model {
        public Integer getLegajo() {
            return getInteger("legajo");
        }

        public void setLegajo(Integer legajo) {
            set("legajo", legajo);
        }

        public String getNombre() {
            return getString("nombre");
        }

        public void setNombre(String nombre) {
            set("nombre", nombre);
        }

        public String getApellido() {
            return getString("apellido");
        }

        public void setApellido(String apellido) {
            set("apellido", apellido);
        }

        public String getDni() {
            return getString("dni");
        }

        public void setDni(String dni) {
            set("dni", dni);
        }

        public String getEmail() {
            return getString("email");
        }

        public void setEmail(String email) {
            set("email", email);
        }

        public String getTelefono() {
            return getString("telefono");
        }

        public void setTelefono(String telefono) {
            set("telefono", telefono);
        }
    }

    @Override
    public List<Profesor> findAll() {
        return ProfesorEntity.findAll().stream()
            .map(entity -> mapToModel((ProfesorEntity) entity))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Profesor> findByLegajo(Integer legajo) {
        ProfesorEntity entity = ProfesorEntity.findFirst("legajo = ?", legajo);
        return Optional.ofNullable(entity).map(this::mapToModel);
    }

    @Override
    public List<Profesor> findByApellido(String apellido) {
        return ProfesorEntity.find("apellido like ?", "%" + apellido + "%").stream()
            .map(entity -> mapToModel((ProfesorEntity) entity))
            .collect(Collectors.toList());
    }

    @Override
    public void save(Profesor profesor) {
        if (exists(profesor.getLegajo())) {
            throw new IllegalArgumentException("Ya existe un profesor con el legajo " + profesor.getLegajo());
        }
        ProfesorEntity entity = new ProfesorEntity();
        mapToEntity(profesor, entity);
        entity.save();
    }

    @Override
    public void update(Profesor profesor) {
        ProfesorEntity entity = ProfesorEntity.findFirst("legajo = ?", profesor.getLegajo());
        if (entity == null) {
            throw new IllegalArgumentException("No existe un profesor con el legajo " + profesor.getLegajo());
        }
        mapToEntity(profesor, entity);
        entity.save();
    }

    @Override
    public boolean delete(Integer legajo) {
        ProfesorEntity entity = ProfesorEntity.findFirst("legajo = ?", legajo);
        if (entity != null) {
            return entity.delete();
        }
        return false;
    }

    @Override
    public boolean exists(Integer legajo) {
        return ProfesorEntity.findFirst("legajo = ?", legajo) != null;
    }

    @Override
    public boolean existsByDni(String dni) {
        return ProfesorEntity.findFirst("dni = ?", dni) != null;
    }

    /**
     * Convierte una entidad de la base de datos en un objeto del modelo de dominio.
     * @param entity Entidad a convertir
     * @return Objeto del modelo de dominio
     */
    private Profesor mapToModel(ProfesorEntity entity) {
        return new Profesor(
            entity.getLegajo(),
            entity.getNombre(),
            entity.getApellido(),
            entity.getDni(),
            entity.getEmail(),
            entity.getTelefono()
        );
    }

    /**
     * Mapea los datos de un objeto del modelo de dominio a una entidad de la base de datos.
     * @param model Objeto del modelo de dominio
     * @param entity Entidad a actualizar
     */
    private void mapToEntity(Profesor model, ProfesorEntity entity) {
        entity.setLegajo(model.getLegajo());
        entity.setNombre(model.getNombre());
        entity.setApellido(model.getApellido());
        entity.setDni(model.getDni());
        entity.setEmail(model.getEmail());
        entity.setTelefono(model.getTelefono());
    }
}