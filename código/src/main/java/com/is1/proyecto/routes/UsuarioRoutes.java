package com.is1.proyecto.routes;

import com.is1.proyecto.controllers.UsuarioController;
import com.is1.proyecto.repositories.*; // Importa todos los repositorios
import com.is1.proyecto.services.UsuarioService;
import com.is1.proyecto.config.AuthMiddleware;
import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.*;

/**
 * Configura las rutas para la gestión de Usuarios (CRUD).
 * Protegido por el rol de Administrador.
 */
public class UsuarioRoutes {

    public static void configure() {
        
        // --- Inyección de Dependencias (aquí se conecta todo) ---
        
        // 1. Instanciar todos los repositorios necesarios
        UserRepository userRepo = new ActiveJDBCUserRepository();
        PersonaRepository personaRepo = new ActiveJDBC_PersonaRepository();
        ProfesorRepository profesorRepo = new ActiveJDBC_ProfesorRepository();
        EstudianteRepository estudianteRepo = new ActiveJDBC_EstudianteRepository();

        // 2. Instanciar el Servicio y pasarle sus dependencias
        UsuarioService usuarioService = new UsuarioService(userRepo, personaRepo, profesorRepo, estudianteRepo);
        
        // 3. Instanciar el Controlador
        UsuarioController controller = new UsuarioController(usuarioService);
        
        MustacheTemplateEngine engine = new MustacheTemplateEngine();

        // --- Definición de Rutas para Usuario ---
        
        // Usamos un grupo de rutas para aplicar el middleware a todo lo que esté dentro
        path("/admin/usuarios", () -> {
            
            // Filtro de Autorización: Solo Admins
            before("/*", AuthMiddleware.requireAdmin);

            // 1. (VISTA) Mostrar el formulario de creación
            get("/nuevo", controller::showCreateUserForm, engine);

            // 2. (PROCESAR) Guardar el nuevo usuario
            post("/guardar", controller::saveUser);
            
            // (Aquí irían las rutas /listar, /:id/editar, etc.)
        });
    }
}