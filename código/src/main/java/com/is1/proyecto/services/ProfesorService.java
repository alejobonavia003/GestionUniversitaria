package com.is1.proyecto.services;

import com.is1.proyecto.models.Cursa;
import com.is1.proyecto.models.Dicta;
import com.is1.proyecto.models.NotaFinal;
import com.is1.proyecto.repositories.CursaRepository;
import com.is1.proyecto.repositories.DictaRepository;
import com.is1.proyecto.repositories.NotaFinalRepository;
import com.is1.proyecto.utils.LoggerUtil;
import org.slf4j.Logger;
import spark.Request;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la lógica de negocio del Portal del Profesor.
 */
public class ProfesorService {
    private static final Logger logger = LoggerUtil.getLogger(ProfesorService.class);

    private final DictaRepository dictaRepository;
    private final CursaRepository cursaRepository;
    private final NotaFinalRepository notaFinalRepository;

    public ProfesorService(DictaRepository dictaRepository, CursaRepository cursaRepository, NotaFinalRepository notaFinalRepository) {
        this.dictaRepository = dictaRepository;
        this.cursaRepository = cursaRepository;
        this.notaFinalRepository = notaFinalRepository;
    }

    /**
     * Obtiene las asignaciones (Dicta) de un profesor.
     * @param dniProfesor El DNI del profesor logueado.
     * @return Lista de sus asignaciones (materias, periodos, cargos).
     */
    public List<Dicta> getMisMateriasAsignadas(long dniProfesor) {
        return dictaRepository.findByProfesorDni(dniProfesor);
    }

    /**
     * Obtiene el listado de alumnos inscritos (Cursa) en una materia.
     * @param codigoMateria El código de la materia.
     * @return Lista de inscripciones (Cursa).
     */
    public List<Cursa> getAlumnosPorMateria(long codigoMateria) {
        return cursaRepository.findByMateriaId(codigoMateria);
    }

    /**
     * Carga o actualiza la nota final de un alumno.
     * @param req La petición Spark con los datos del formulario de notas.
     */
    public void cargarNota(Request req) throws Exception {
        long dniEstudiante = Long.parseLong(req.queryParams("dni_estudiante"));
        long codMateria = Long.parseLong(req.queryParams("codigo_materia"));
        int notaValor = Integer.parseInt(req.queryParams("nota"));
        String condicion = req.queryParams("condicion");

        // Validación de negocio
        if (notaValor < 0 || notaValor > 10) {
            throw new Exception("La nota debe estar entre 0 y 10.");
        }
        if (condicion == null || condicion.trim().isEmpty()) {
            throw new Exception("La condición (Regular, Aprobado, Libre) no puede estar vacía.");
        }

        // Buscamos si la nota ya existe (para actualizar) o si es nueva
        Optional<NotaFinal> notaExistente = notaFinalRepository.findById(dniEstudiante, codMateria);
        
        NotaFinal notaFinal;
        if (notaExistente.isPresent()) {
            // Actualiza la nota existente
            notaFinal = notaExistente.get();
            logger.info("Actualizando nota para Estudiante DNI: {}, Materia: {}", dniEstudiante, codMateria);
        } else {
            // Crea una nueva nota
            notaFinal = new NotaFinal();
            notaFinal.set("dni", dniEstudiante);
            notaFinal.set("codigo_materia", codMateria);
            logger.info("Cargando nueva nota para Estudiante DNI: {}, Materia: {}", dniEstudiante, codMateria);
        }

        notaFinal.set("nota", notaValor);
        notaFinal.set("condicion", condicion);

        notaFinalRepository.save(notaFinal);
    }
}