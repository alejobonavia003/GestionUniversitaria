package com.is1.proyecto.services;

import com.is1.proyecto.models.Dicta;
import com.is1.proyecto.repositories.DictaRepository;
import com.is1.proyecto.repositories.MateriaRepository;
import com.is1.proyecto.repositories.PeriodoRepository;
import com.is1.proyecto.repositories.ProfesorRepository;
import com.is1.proyecto.utils.LoggerUtil;
import org.slf4j.Logger;
import spark.Request;

import java.util.List;

/**
 * Servicio para la lógica de negocio de las Asignaciones Docentes (Dicta).
 */
public class DictaService {
    private static final Logger logger = LoggerUtil.getLogger(DictaService.class);

    private final DictaRepository dictaRepository;
    private final ProfesorRepository profesorRepository;
    private final MateriaRepository materiaRepository;
    private final PeriodoRepository periodoRepository;

    public DictaService(DictaRepository dictaRepository, ProfesorRepository profesorRepository, MateriaRepository materiaRepository, PeriodoRepository periodoRepository) {
        this.dictaRepository = dictaRepository;
        this.profesorRepository = profesorRepository;
        this.materiaRepository = materiaRepository;
        this.periodoRepository = periodoRepository;
    }

    /**
     * Obtiene todas las asignaciones de una materia específica.
     * @param codigoMateria El código de la materia.
     * @return Lista de asignaciones.
     */
    public List<Dicta> getAsignacionesPorMateria(long codigoMateria) {
        return dictaRepository.findByMateriaId(codigoMateria);
    }

    /**
     * Asigna un Profesor a una Materia en un Periodo con un Cargo/Participación.
     * @param req La petición Spark con los datos del formulario.
     * @throws Exception Si la validación falla.
     */
    public void asignarProfesor(Request req) throws Exception {
        long dniProf = Long.parseLong(req.queryParams("dni_prof"));
        long codMateria = Long.parseLong(req.queryParams("codigo_materia"));
        long idPeriodo = Long.parseLong(req.queryParams("id_periodo"));
        String cargo = req.queryParams("cargo");
        String participacion = req.queryParams("participacion");

        // --- Validaciones de Negocio ---
        if (profesorRepository.findByDni(dniProf).isEmpty()) {
            throw new Exception("El Profesor con DNI " + dniProf + " no existe.");
        }
        if (materiaRepository.findById(codMateria).isEmpty()) {
            throw new Exception("La Materia con Código " + codMateria + " no existe.");
        }
        if (periodoRepository.findById(idPeriodo).isEmpty()) {
            throw new Exception("El Periodo ID " + idPeriodo + " no existe.");
        }

        // Crear la entidad de asignación (clave compuesta)
        Dicta nuevaAsignacion = new Dicta();
        nuevaAsignacion.set("dni_prof", dniProf);
        nuevaAsignacion.set("codigo_materia", codMateria);
        nuevaAsignacion.set("id_periodo", idPeriodo);
        
        // Atributos de la relación
        nuevaAsignacion.set("cargo", cargo);
        nuevaAsignacion.set("participacion", participacion);
        
        dictaRepository.save(nuevaAsignacion);
        logger.info("Profesor {} asignado a Materia {} en Periodo {} exitosamente.", dniProf, codMateria, idPeriodo);
    }

    /**
     * Elimina (desasigna) un profesor de una materia/periodo.
     * @param dniProf DNI del profesor.
     * @param codMateria Código de la materia.
     * @param idPeriodo ID del periodo.
     */
    public void desasignarProfesor(long dniProf, long codMateria, long idPeriodo) throws Exception {
        boolean deleted = dictaRepository.deleteById(dniProf, codMateria, idPeriodo);
        if (!deleted) {
            throw new Exception("No se encontró la asignación para eliminar.");
        }
        logger.info("Profesor {} desasignado de Materia {} en Periodo {}.", dniProf, codMateria, idPeriodo);
    }
}