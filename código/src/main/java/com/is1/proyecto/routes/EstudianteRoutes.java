package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.EstudianteController;
import com.is1.proyecto.repositories.*; // Importa todos los repos
import com.is1.proyecto.services.EstudianteService;
import com.is1.proyecto.config.AuthMiddleware;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.*;

/**
 * Configura las rutas para el Portal del Estudiante (Rol: ESTUDIANTE).
 */
public class EstudianteRoutes {

    public static void configure() {

        // --- Inyección de Dependencias ---
        NotaFinalRepository notaRepo = new ActiveJDBC_NotaFinalRepository();
        CursaRepository cursaRepo = new ActiveJDBC_CursaRepository();
        MateriaRepository materiaRepo = new ActiveJDBC_MateriaRepository();
        
        EstudianteService estudianteService = new EstudianteService(notaRepo, cursaRepo, materiaRepo);
        
        EstudianteController controller = new EstudianteController(estudianteService, materiaRepo);

        MustacheTemplateEngine engine = new MustacheTemplateEngine();

        // --- Definición de Rutas para Estudiantes ---
        
        path("/estudiante", () -> {
            
            // Filtro de Autorización: Solo Estudiantes (o Admins)
            before("/*", AuthMiddleware.requireEstudiante);

            // 1. (VISTA) Ver el historial académico (analítico)
            get("/historial", controller::showHistorial, engine);

            // 2. (VISTA) Ver formulario de inscripción a materias
            get("/inscripciones", controller::showInscripciones, engine);

            // 3. (PROCESAR) Inscribirse a una materia
            post("/inscribir", controller::procesarInscripcion);
        });
    }
}