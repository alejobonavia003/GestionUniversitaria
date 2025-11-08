package com.is1.proyecto.services;

import com.is1.proyecto.models.Cursa;
import com.is1.proyecto.models.Materia;
import com.is1.proyecto.models.NotaFinal;
import com.is1.proyecto.repositories.CursaRepository;
import com.is1.proyecto.repositories.MateriaRepository;
import com.is1.proyecto.repositories.NotaFinalRepository;
import com.is1.proyecto.utils.LoggerUtil;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio para la lógica de negocio del rol ESTUDIANTE.
 * Maneja inscripciones (Cursa) y consulta de notas (NotaFinal),
 * incluyendo la validación de correlativas.
 */
public class EstudianteService {
    private static final Logger logger = LoggerUtil.getLogger(EstudianteService.class);

    private final NotaFinalRepository notaFinalRepository;
    private final CursaRepository cursaRepository;
    private final MateriaRepository materiaRepository;

    public EstudianteService(NotaFinalRepository notaFinalRepository, CursaRepository cursaRepository, MateriaRepository materiaRepository) {
        this.notaFinalRepository = notaFinalRepository;
        this.cursaRepository = cursaRepository;
        this.materiaRepository = materiaRepository;
    }

    /**
     * Obtiene el historial académico (todas las notas finales) de un estudiante.
     * @param dniEstudiante DNI del estudiante.
     * @return Lista de NotaFinal (Rindio).
     */
    public List<NotaFinal> getHistorialAcademico(long dniEstudiante) {
        return notaFinalRepository.findByEstudianteDni(dniEstudiante);
    }

    /**
     * Obtiene las inscripciones actuales (materias que cursa) de un estudiante.
     * @param dniEstudiante DNI del estudiante.
     * @return Lista de Cursa.
     */
    public List<Cursa> getInscripcionesActuales(long dniEstudiante) {
        return cursaRepository.findByEstudianteDni(dniEstudiante);
    }

    /**
     * Inscribe a un estudiante a una materia, validando las correlativas.
     * @param dniEstudiante DNI del estudiante.
     * @param codigoMateria Código de la materia a inscribir.
     * @throws Exception Si la inscripción falla (ya inscripto o faltan correlativas).
     */
    public void inscribirAMateria(long dniEstudiante, long codigoMateria) throws Exception {
        logger.debug("Intento de inscripción DNI: {} a Materia: {}", dniEstudiante, codigoMateria);

        // 1. Verificar si ya está inscripto
        if (cursaRepository.findByEstudianteDni(dniEstudiante).stream().anyMatch(c -> c.getCodigoMateria() == codigoMateria)) {
            throw new Exception("El estudiante ya está inscripto en esta materia.");
        }

        // 2. Obtener la materia y sus correlativas
        Optional<Materia> materiaOpt = materiaRepository.findById(codigoMateria);
        if (materiaOpt.isEmpty()) {
            throw new Exception("La materia no existe.");
        }
        
        // Usamos el método getCorrelativas() del modelo Materia
        LazyList<Materia> correlativas = materiaOpt.get().getAll(Materia.class);
        
        if (correlativas.isEmpty()) {
            // No tiene correlativas, se inscribe directamente
            logger.info("Materia sin correlativas. Inscribiendo...");
            Cursa nuevaInscripcion = new Cursa();
            nuevaInscripcion.set("dni", dniEstudiante);
            nuevaInscripcion.set("codigo_materia", codigoMateria);
            cursaRepository.save(nuevaInscripcion);
            return;
        }

        // 3. Obtener las materias aprobadas por el estudiante
        List<NotaFinal> historial = getHistorialAcademico(dniEstudiante);
        
        // Creamos un Set (conjunto) con los IDs de las materias APROBADAS
        Set<Object> materiasAprobadasIds = historial.stream()
            .filter(nota -> "Aprobado".equalsIgnoreCase(nota.getString("condicion"))) // ¡OJO! Asegúrate que "Aprobado" sea el string correcto
            .map(nota -> nota.get("codigo_materia"))
            .collect(Collectors.toSet());

        // 4. Validar correlativas
        for (Materia correlativa : correlativas) {
            if (!materiasAprobadasIds.contains(correlativa.getId())) {
                logger.warn("Inscripción fallida. Falta correlativa: {}", correlativa.getString("nombre"));
                throw new Exception("Inscripción fallida. Falta aprobar la materia correlativa: " + correlativa.getString("nombre"));
            }
        }

        // 5. Si pasó todas las validaciones, se inscribe
        logger.info("Validación de correlativas exitosa. Inscribiendo DNI: {} a Materia: {}", dniEstudiante, codigoMateria);
        Cursa nuevaInscripcion = new Cursa();
        nuevaInscripcion.set("dni", dniEstudiante);
        nuevaInscripcion.set("codigo_materia", codigoMateria);
        cursaRepository.save(nuevaInscripcion);
    }
}