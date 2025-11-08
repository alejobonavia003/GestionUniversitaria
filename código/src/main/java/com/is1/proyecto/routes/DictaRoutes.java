package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.DictaController;
import com.is1.proyecto.repositories.*; // Importa todos los repos
import com.is1.proyecto.services.DictaService;
import com.is1.proyecto.services.MateriaService;
import com.is1.proyecto.config.AuthMiddleware;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.*;

/**
 * Configura las rutas para la gestión de Asignaciones Docentes (Dicta).
 * Protegido por el rol de Administrador.
 */
public class DictaRoutes {

    public static void configure() {
        
        // --- Inyección de Dependencias ---
        DictaRepository dictaRepo = new ActiveJDBC_DictaRepository();
        ProfesorRepository profesorRepo = new ActiveJDBC_ProfesorRepository();
        MateriaRepository materiaRepo = new ActiveJDBC_MateriaRepository();
        PeriodoRepository periodoRepo = new ActiveJDBC_PeriodoRepository();
        
        // Asumimos que MateriaService solo necesita su repo y el de PlanEstudio (null por ahora)
        MateriaService materiaService = new MateriaService(materiaRepo, null); 
        
        DictaService dictaService = new DictaService(dictaRepo, profesorRepo, materiaRepo, periodoRepo);
        
        DictaController controller = new DictaController(dictaService, materiaService, profesorRepo, periodoRepo);

        MustacheTemplateEngine engine = new MustacheTemplateEngine();

        // --- Definición de Rutas para Asignaciones ---
        
        // Usamos un grupo para aplicar el middleware
        path("/admin", () -> {
            
            // Filtro de Autorización: Solo Admins
            before("/materias/:id/asignaciones", AuthMiddleware.requireAdmin);
            before("/asignaciones/*", AuthMiddleware.requireAdmin);

            // 1. (VISTA) Mostrar el panel/formulario de asignación para una materia
            get("/materias/:id/asignaciones", controller::showAsignacionForm, engine);

            // 2. (PROCESAR) Guardar la nueva asignación
            post("/asignaciones/guardar", controller::asignarProfesor);

            // 3. (PROCESAR) Eliminar una asignación
            post("/asignaciones/eliminar", controller::desasignarProfesor);
        });
    }
}
