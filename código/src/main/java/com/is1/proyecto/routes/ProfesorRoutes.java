package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.ProfesorController;
import com.is1.proyecto.repositories.*; // Importa todos los repos
import com.is1.proyecto.services.ProfesorService;
import com.is1.proyecto.config.AuthMiddleware;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.*;

/**
 * Configura las rutas para el Portal del Profesor (Rol: PROFESOR).
 */
public class ProfesorRoutes {

    public static void configure() {
        
        // --- Inyección de Dependencias ---
        DictaRepository dictaRepo = new ActiveJDBC_DictaRepository();
        CursaRepository cursaRepo = new ActiveJDBC_CursaRepository();
        NotaFinalRepository notaRepo = new ActiveJDBC_NotaFinalRepository();
        MateriaRepository materiaRepo = new ActiveJDBC_MateriaRepository(); // El controller lo necesita
        
        ProfesorService profesorService = new ProfesorService(dictaRepo, cursaRepo, notaRepo);
        
        ProfesorController controller = new ProfesorController(profesorService, materiaRepo);

        MustacheTemplateEngine engine = new MustacheTemplateEngine();

        // --- Definición de Rutas para Profesores ---
        
        // Usamos un grupo para aplicar el middleware a todas las rutas /profesor
        path("/profesor", () -> {
            
            // Filtro de Autorización: Solo Profesores (o Admins, según la lógica del middleware)
            before("/*", AuthMiddleware.requireProfesor);

            // 1. (VISTA) Ver las materias que dicta
            get("/mis-materias", controller::showMisMaterias, engine);

            // 2. (VISTA) Ver la planilla de carga de notas para una materia
            get("/materia/:id/notas", controller::showCargarNotas, engine);

            // 3. (PROCESAR) Guardar una nota (o varias)
            post("/notas/guardar", controller::procesarCargarNotas);
        });
    }
}