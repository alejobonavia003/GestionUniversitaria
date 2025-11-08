package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.CarreraController;
import com.is1.proyecto.repositories.ActiveJDBC_CarreraRepository;
import com.is1.proyecto.repositories.CarreraRepository;
import com.is1.proyecto.services.CarreraService;
import com.is1.proyecto.config.AuthMiddleware; // ¡Importamos el Middleware!
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.*;

/**
 * Define las rutas (URLs) para el módulo de gestión de Carreras.
 * Todas las rutas en este archivo están protegidas y requieren rol de ADMIN.
 */
public class CarreraRoutes {

    public static void configure() {
        
        // 1. Inicialización de dependencias (Inyección Manual)
        CarreraRepository carreraRepository = new ActiveJDBC_CarreraRepository();
        CarreraService carreraService = new CarreraService(carreraRepository);
        CarreraController carreraController = new CarreraController(carreraService);

        // 2. Definición del motor de plantillas
        MustacheTemplateEngine engine = new MustacheTemplateEngine();

        // --- Definición de Rutas del CRUD de Carreras ---
        // Todas estas rutas están bajo el prefijo /admin (o el que definas)
        // y están protegidas por el Middleware de Administrador.

        // [LISTAR] - GET /admin/carreras
        // (Usamos /carreras como ejemplo, puedes agruparlas bajo /admin/carreras)
        get("/admin/carreras", carreraController::listCarreras, engine);

        // [CREAR - Mostrar Formulario] - GET /admin/carreras/nueva
        get("/admin/carreras/nueva", carreraController::showCreateForm, engine);
        
        // [CREAR - Procesar Formulario] - POST /admin/carreras/nueva
        post("/admin/carreras/nueva", carreraController::createCarrera);

        // [EDITAR - Mostrar Formulario] - GET /admin/carreras/:id/editar
        get("/admin/carreras/:id/editar", carreraController::showEditForm, engine);
        
        // [EDITAR - Procesar Formulario] - POST /admin/carreras/:id/editar
        post("/admin/carreras/:id/editar", carreraController::updateCarrera);
        
        // [ELIMINAR] - POST /admin/carreras/:id/eliminar
        post("/admin/carreras/:id/eliminar", carreraController::deleteCarrera);


        // --- APLICACIÓN DEL MIDDLEWARE ---
        // Aplicamos el filtro requireAdmin A TODAS las rutas que empiecen con "/admin/carreras"
        // Esto debe hacerse después de definir las rutas o en el archivo principal de configuración de Spark.
        before("/admin/carreras/*", AuthMiddleware.requireAdmin);
    }
}