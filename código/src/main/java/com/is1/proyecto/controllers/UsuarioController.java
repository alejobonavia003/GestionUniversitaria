package com.is1.proyecto.controllers;

import com.is1.proyecto.services.UsuarioService;
import com.is1.proyecto.config.AuthMiddleware;
import com.is1.proyecto.utils.LoggerUtil;
import org.slf4j.Logger;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.Map;

/**
 * Controlador para la gestión de creación de Usuarios (Estudiantes, Profesores).
 * Protegido por rol de ADMIN.
 */
public class UsuarioController {

    private static final Logger logger = LoggerUtil.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Muestra el formulario unificado para crear un nuevo usuario (Estudiante o Profesor).
     * Ruta: GET /admin/usuarios/nuevo
     */
    public ModelAndView showCreateUserForm(Request req, Response res) {
        Map<String, Object> model = AuthMiddleware.getBaseModel(req);
        
        model.put("pageTitle", "Crear Nuevo Usuario");
        
        // Mensajes de feedback (si venimos de un error)
        model.put("errorMessage", req.queryParams("error"));
        model.put("successMessage", req.queryParams("success"));

        // Usará la vista 'user_form.mustache' que ya tienes.
        // Asumo que está en 'templates/admin/usuarios/formulario.mustache'
        return new ModelAndView(model, "admin/usuarios/formulario.mustache");
    }

    /**
     * Procesa el formulario de creación de un nuevo usuario.
     * Ruta: POST /admin/usuarios/guardar
     */
    public Object saveUser(Request req, Response res) {
        String redirectTo = "/admin/usuarios/nuevo"; // Ruta del formulario

        try {
            // Toda la lógica compleja (validación, 3 inserts, transacción)
            // está delegada en el servicio.
            usuarioService.crearUsuario(req);
            
            logger.info("Usuario creado exitosamente desde el controlador.");
            res.redirect(redirectTo + "?success=Usuario creado exitosamente");
            
        } catch (Exception e) {
            // Si el servicio lanza una excepción (ej: DNI duplicado, error de DB)
            logger.warn("Error al crear usuario: {}", e.getMessage());
            res.redirect(redirectTo + "?error=" + e.getMessage());
        }
        
        return null;
    }
    
    // Aquí irían los métodos para listar, editar y eliminar usuarios,
    // pero por ahora nos centramos en la creación.
}