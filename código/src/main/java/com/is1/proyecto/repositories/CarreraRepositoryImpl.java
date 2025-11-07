package com.is1.proyecto.repositories;

import com.is1.proyecto.models.Carrera;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del repositorio de Carrera usando ActiveJDBC.
 * fujo de datos tipico controller -> servicio -> repositorio -> modelo
 * 
 * entonces repositorio se encarga de 
 * mapear entre el modelo de dominio y la entidad de la base de datos
 * 
 * esta capa se comunica directamente con la base de datos
 * usa patron repository para abstraer operaciones CRUD
 */
@Table("Carrera")
public class CarreraRepositoryImpl implements CarreraRepository {
    
    private static class CarreraEntity extends Model {
        public Integer getCodigo() {
            return getInteger("cod_carrera");
        }

        public void setCodigo(Integer codigo) {
            set("cod_carrera", codigo);
        }

        public String getNombre() {
            return getString("nombre");
        }

        public void setNombre(String nombre) {
            set("nombre", nombre);
        }

        public Integer getDuracion() {
            return getInteger("duracion");
        }

        public void setDuracion(Integer duracion) {
            set("duracion", duracion);
        }
    }

    @Override
    public List<Carrera> findAll() {
        return CarreraEntity.findAll().stream()
            .map(this::mapToModel)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Carrera> findById(Integer codigo) {
        CarreraEntity entity = CarreraEntity.findFirst("cod_carrera = ?", codigo);
        return Optional.ofNullable(entity).map(this::mapToModel);
    }

    @Override
    public void save(Carrera carrera) {
        CarreraEntity entity = new CarreraEntity();
        mapToEntity(carrera, entity);
        entity.save();
    }

    @Override
    public void update(Carrera carrera) {
        CarreraEntity entity = CarreraEntity.findFirst("cod_carrera = ?", carrera.getCodigo());
        if (entity != null) {
            mapToEntity(carrera, entity);
            entity.save();
        }
    }

    @Override
    public void delete(Integer codigo) {
        CarreraEntity entity = CarreraEntity.findFirst("cod_carrera = ?", codigo);
        if (entity != null) {
            entity.delete();
        }
    }

    @Override
    public boolean exists(Integer codigo) {
        return CarreraEntity.findFirst("cod_carrera = ?", codigo) != null;
    }

    private Carrera mapToModel(CarreraEntity entity) {
        return new Carrera(
            entity.getCodigo(),
            entity.getNombre(),
            entity.getDuracion()
        );
    }

    private void mapToEntity(Carrera model, CarreraEntity entity) {
        entity.setCodigo(model.getCodigo());
        entity.setNombre(model.getNombre());
        entity.setDuracion(model.getDuracion());
    }
}