package com.is1.proyecto.services;

import com.is1.proyecto.models.Estudiante;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.Profesor;
import com.is1.proyecto.models.User;
import com.is1.proyecto.repositories.EstudianteRepository;
import com.is1.proyecto.repositories.PersonaRepository;
import com.is1.proyecto.repositories.ProfesorRepository;
import com.is1.proyecto.repositories.UserRepository;
import com.is1.proyecto.utils.LoggerUtil;
import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import spark.Request;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio para la lógica de negocio de la Gestión de Usuarios (Personas).
 * Maneja la creación transaccional de Estudiantes y Profesores.
 */
public class UsuarioService {
    private static final Logger logger = LoggerUtil.getLogger(UsuarioService.class);

    private final UserRepository userRepository;
    private final PersonaRepository personaRepository;
    private final ProfesorRepository profesorRepository;
    private final EstudianteRepository estudianteRepository;

    public UsuarioService(UserRepository userRepository, PersonaRepository personaRepository, ProfesorRepository profesorRepository, EstudianteRepository estudianteRepository) {
        this.userRepository = userRepository;
        this.personaRepository = personaRepository;
        this.profesorRepository = profesorRepository;
        this.estudianteRepository = estudianteRepository;
    }

    /**
     * Crea un nuevo usuario (Persona, Rol y User) en una transacción.
     * @param req La petición Spark con los datos del formulario.
     * @throws Exception Si la validación falla o la transacción es revertida.
     */
    public void crearUsuario(Request req) throws Exception {
        long dni = Long.parseLong(req.queryParams("dni"));
        String username = req.queryParams("username");
        String pass = req.queryParams("password");
        String role = req.queryParams("role"); // "PROFESOR" o "ESTUDIANTE"

        // --- Validación ---
        if (personaRepository.findByDni(dni).isPresent()) {
            throw new Exception("El DNI " + dni + " ya está registrado.");
        }
        if (userRepository.findUserByUsername(username).isPresent()) {
            throw new Exception("El nombre de usuario '" + username + "' ya existe.");
        }

        // --- Transacción ---
        try {
            Base.openTransaction();
            logger.info("Iniciando transacción para crear usuario DNI: {}", dni);

            // 1. Crear Persona (Datos comunes)
            Persona persona = new Persona();
            persona.set("dni", dni); // PK
            persona.set("nombre", req.queryParams("nombre"));
            persona.set("apellido", req.queryParams("apellido"));
            persona.set("email", req.queryParams("email"));
            personaRepository.save(persona);

            // 2. Crear Rol (Estudiante o Profesor)
            if ("PROFESOR".equals(role)) {
                Profesor profesor = new Profesor();
                profesor.set("dni", dni); // PK/FK
                profesor.set("id_doc", req.queryParams("id_doc")); // Legajo de profesor
                profesorRepository.save(profesor);
            
            } else if ("ESTUDIANTE".equals(role)) {
                Estudiante estudiante = new Estudiante();
                estudiante.set("dni", dni); // PK/FK
                estudiante.set("legajo", req.queryParams("legajo")); // Legajo de estudiante
                estudianteRepository.save(estudiante);
            
            } else {
                throw new Exception("Rol desconocido: " + role);
            }

            // 3. Crear User (Datos de Login)
            User user = new User();
            user.set("name", username);
            user.set("password", BCrypt.hashpw(pass, BCrypt.gensalt()));
            user.set("role", role);
            userRepository.save(user);

            Base.commitTransaction();
            logger.info("Usuario DNI: {} creado exitosamente.", dni);

        } catch (Exception e) {
            Base.rollbackTransaction();
            logger.error("Error al crear usuario, transacción revertida: {}", e.getMessage(), e);
            throw e; // Re-lanza la excepción para que el controlador la atrape
        }
    }

    // --- Otros métodos (Listar, Editar, Borrar) ---
    // (Estos los implementaremos después)

}