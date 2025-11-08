package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.MateriaController;
import com.is1.proyecto.repositories.ActiveJDBC_MateriaRepository;
import com.is1.proyecto.repositories.ActiveJDBC_PlanEstudioRepository;
import com.is1.proyecto.repositories.MateriaRepository;
import com.is1.proyecto.repositories.PlanEstudioRepository;
import com.is1.proyecto.services.MateriaService;
import com.is1.proyecto.services.PlanEstudioService;

import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.*;

/**
 * Configura las rutas para la gestión de Materias (CRUD).
 * Protegido por el rol de Administrador.
 */
public class MateriaRoutes {

    public static void configure() {
        
        // --- Inyección de Dependencias ---
        MateriaRepository materiaRepo = new ActiveJDBC_MateriaRepository();
        PlanEstudioRepository planRepo = new ActiveJDBC_PlanEstudioRepository();
        
        PlanEstudioService planService = new PlanEstudioService(planRepo, null); // El servicio de Materia no necesita el CarreraRepo
        MateriaService materiaService = new MateriaService(materiaRepo, planRepo);
        
        MateriaController controller = new MateriaController(materiaService, planService);
        
        MustacheTemplateEngine engine = new MustacheTemplateEngine();

        // --- Definición de Rutas CRUD para Materia ---
        
        // 1. (LISTAR) Ver todas las materias de un plan específico
        get("/admin/planes/:plan_id/materias", controller::listMateriasByPlan, engine);

        // 2. (CREAR - Formulario)
        get("/admin/planes/:plan_id/materias/nueva", controller::showCreateForm, engine);

        // 3. (EDITAR - Formulario)
        get("/admin/materias/:id/editar", controller::showEditForm, engine);

        // 4. (GUARDAR - POST) Ruta única para crear y actualizar
        post("/admin/materias/guardar", controller::saveMateria);

        // 5. (ELIMINAR - POST por seguridad)
        post("/admin/materias/:id/eliminar", controller::deleteMateria);

        // --- Filtro de Autorización ---
        // (Asumimos que la ruta "/admin/*" ya está protegida en CarreraRoutes o GeneralRoutes)
        // Si no, descomentar:
        // before("/admin/planes/*", AuthMiddleware.requireAdmin);
        // before("/admin/materias/*", AuthMiddleware.requireAdmin);
    }
}