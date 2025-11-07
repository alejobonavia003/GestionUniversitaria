package com.is1.proyecto.services;

import com.is1.proyecto.models.Carrera;
import com.is1.proyecto.repositories.CarreraRepository;
import com.is1.proyecto.dto.CarreraDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

/**
 * Servicio que maneja la lógica de negocio relacionada con las Carreras.
 */
public class CarreraService {
    private final CarreraRepository carreraRepository;

    public CarreraService(CarreraRepository carreraRepository) {
        this.carreraRepository = carreraRepository;
    }

    public List<CarreraDTO> getAllCarreras() {
        return carreraRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CarreraDTO> getCarreraByCodigo(Integer codigo) {
        return carreraRepository.findById(codigo)
                .map(this::convertToDTO);
    }

    public void createCarrera(CarreraDTO carreraDTO) {
        if (carreraRepository.exists(carreraDTO.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una carrera con ese código");
        }
        carreraRepository.save(convertToEntity(carreraDTO));
    }

    public void updateCarrera(CarreraDTO carreraDTO) {
        if (!carreraRepository.exists(carreraDTO.getCodigo())) {
            throw new IllegalArgumentException("No existe una carrera con ese código");
        }
        carreraRepository.update(convertToEntity(carreraDTO));
    }

    public void deleteCarrera(Integer codigo) {
        if (!carreraRepository.exists(codigo)) {
            throw new IllegalArgumentException("No existe una carrera con ese código");
        }
        carreraRepository.delete(codigo);
    }

    // Métodos de conversión entre DTO y entidad
    private CarreraDTO convertToDTO(Carrera carrera) {
        return new CarreraDTO(
            carrera.getCodigo(),
            carrera.getNombre(),
            carrera.getDuracion()
        );
    }

    private Carrera convertToEntity(CarreraDTO dto) {
        return new Carrera(
            dto.getCodigo(),
            dto.getNombre(),
            dto.getDuracion()
        );
    }
}