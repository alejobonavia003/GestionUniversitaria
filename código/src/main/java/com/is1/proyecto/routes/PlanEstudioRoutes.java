
package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.PlanEstudioController;
import com.is1.proyecto.repositories.ActiveJDBC_CarreraRepository;
import com.is1.proyecto.repositories.ActiveJDBC_PlanEstudioRepository;
import com.is1.proyecto.services.CarreraService;
import com.is1.proyecto.services.PlanEstudioService;
import com.is1.proyecto.config.AuthMiddleware;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.*;

/**
 * Configura las rutas para la gestión de Planes de Estudio (CRUD).
 * Protegido por el rol de Administrador.
 */
public class PlanEstudioRoutes {

    public static void configure() {
        
        // --- Inyección de Dependencias ---
        // (En un framework más grande, esto lo haría un Inyector de Dependencias)
        ActiveJDBC_PlanEstudioRepository planRepo = new ActiveJDBC_PlanEstudioRepository();
        ActiveJDBC_CarreraRepository carreraRepo = new ActiveJDBC_CarreraRepository();
        
        CarreraService carreraService = new CarreraService(carreraRepo);
        PlanEstudioService planService = new PlanEstudioService(planRepo, carreraRepo);
        
        PlanEstudioController controller = new PlanEstudioController(planService, carreraService);
        
        MustacheTemplateEngine engine = new MustacheTemplateEngine();

        // --- Definición de Rutas CRUD para PlanEstudio ---
        
        // 1. (LISTAR) Ver todos los planes de una carrera específica
        // Esta ruta anidada es RESTful
        get("/admin/carreras/:carrera_id/planes", controller::listPlanesByCarrera, engine);

        // 2. (CREAR - Formulario)
        get("/admin/carreras/:carrera_id/planes/nuevo", controller::showCreateForm, engine);

        // 3. (EDITAR - Formulario)
        get("/admin/planes/:id/editar", controller::showEditForm, engine);

        // 4. (GUARDAR - POST) Ruta única para crear y actualizar
        post("/admin/planes/guardar", controller::savePlan);

        // 5. (ELIMINAR)
        get("/admin/planes/:id/eliminar", controller::deletePlan);

        // --- Filtro de Autorización ---
        // Aplicamos el middleware de ADMIN a TODAS las rutas que comiencen con /admin/
        // (Esto ya debería estar en tu archivo principal o en CarreraRoutes, 
        // pero lo ponemos aquí para asegurar)
        before("/admin/carreras/*", AuthMiddleware.requireAdmin);
        before("/admin/planes/*", AuthMiddleware.requireAdmin);
    }
}