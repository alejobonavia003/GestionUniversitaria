package com.is1.proyecto.services;

import com.is1.proyecto.models.Carrera;
import com.is1.proyecto.models.PlanEstudio;
import com.is1.proyecto.repositories.CarreraRepository;
import com.is1.proyecto.utils.LoggerUtil;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Capa de Servicio para la entidad Carrera.
 * Contiene la lógica de negocio principal (validaciones, orquestación).
 * Depende de la interfaz CarreraRepository (Inyección de Dependencia).
 */
public class CarreraService {

    private static final Logger logger = LoggerUtil.getLogger(CarreraService.class);
    private final CarreraRepository carreraRepository;

    /**
     * Constructor para la Inyección de Dependencia.
     * @param carreraRepository La implementación del repositorio a utilizar.
     */
    public CarreraService(CarreraRepository carreraRepository) {
        this.carreraRepository = carreraRepository;
    }

    /**
     * Obtiene todas las carreras.
     * @return Lista de todas las carreras.
     */
    public List<Carrera> getAllCarreras() {
        logger.info("Obteniendo todas las carreras.");
        return carreraRepository.findAll();
    }

    /**
     * Obtiene una carrera por su ID (código).
     * @param id El código de la carrera.
     * @return Un Optional con la carrera, o vacío si no se encuentra.
     */
    public Optional<Carrera> getCarreraById(int id) {
        logger.debug("Buscando carrera con ID: {}", id);
        return carreraRepository.findById(id);
    }

    /**
     * Lógica de negocio para crear una nueva carrera.
     * @param codCarrera El código único para la nueva carrera.
     * @param nombre El nombre de la carrera.
     * @param duracion La duración en años.
     * @return La entidad Carrera creada.
     * @throws IllegalArgumentException si los datos son inválidos o el código ya existe.
     */
    public Carrera createCarrera(int codCarrera, String nombre, int duracion) {
        logger.info("Intentando crear carrera: {}", nombre);
        
        // REGLA DE NEGOCIO 1: Validar datos de entrada
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la carrera no puede estar vacío.");
        }
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración debe ser un número positivo.");
        }

        // REGLA DE NEGOCIO 2: Asegurarse de que el código no esté en uso
        if (carreraRepository.findById(codCarrera).isPresent()) {
            throw new IllegalArgumentException("Ya existe una carrera con el código: " + codCarrera);
        }

        // Creación del modelo
        Carrera nuevaCarrera = new Carrera();
        // Asumimos que la PK 'cod_carrera' se setea manualmente (no es autoincremental)
        nuevaCarrera.set("cod_carrera", codCarrera); 
        nuevaCarrera.setNombre(nombre);
        nuevaCarrera.setDuracion(duracion);
        
        carreraRepository.save(nuevaCarrera);
        logger.info("Carrera creada con éxito (ID: {}).", codCarrera);
        return nuevaCarrera;
    }

    /**
     * Lógica de negocio para actualizar una carrera existente.
     * @param id El código de la carrera a actualizar.
     * @param nuevoNombre El nuevo nombre.
     * @param nuevaDuracion La nueva duración.
     * @return La entidad Carrera actualizada.
     * @throws RuntimeException si la carrera no se encuentra.
     */
    public Carrera updateCarrera(int id, String nuevoNombre, int nuevaDuracion) {
        logger.info("Actualizando carrera ID: {}", id);
        
        // 1. Encontrar la entidad
        Carrera carrera = carreraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró la carrera con ID: " + id));

        // 2. Validar nuevos datos
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }

        // 3. Actualizar el modelo
        carrera.setNombre(nuevoNombre);
        carrera.setDuracion(nuevaDuracion);
        
        // 4. Guardar (el repositorio maneja el UPDATE)
        carreraRepository.save(carrera);
        return carrera;
    }

    /**
     * Lógica de negocio para eliminar una carrera.
     * @param id El código de la carrera a eliminar.
     * @throws RuntimeException si la carrera no se encuentra.
     * @throws IllegalStateException si la carrera tiene Planes de Estudio asociados.
     */
    public void deleteCarrera(int id) {
        logger.warn("Intentando eliminar carrera ID: {}", id);
        
        Carrera carrera = carreraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró la carrera con ID: " + id));

        // REGLA DE NEGOCIO 3: No eliminar si tiene hijos (Planes de Estudio)
        // (ActiveJDBC carga las relaciones al acceder a ellas)
        List<PlanEstudio> planes = carrera.getAll(PlanEstudio.class);
        
        if (planes != null && !planes.isEmpty()) {
            logger.error("Error al eliminar: La carrera {} tiene {} planes de estudio asociados.", carrera.getNombre(), planes.size());
            throw new IllegalStateException("No se puede eliminar la carrera '" + carrera.getNombre() + 
                                            "' porque tiene planes de estudio asociados.");
        }
        
        carreraRepository.delete(carrera);
        logger.info("Carrera ID: {} eliminada exitosamente.", id);
    }
}