package com.is1.proyecto.routes;

import static spark.Spark.*;
import com.is1.proyecto.controllers.AuthController;
import com.is1.proyecto.repositories.ActiveJDBCUserRepository; 
import com.is1.proyecto.repositories.UserRepository;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

/**
 * La ruta de autenticacion delega todo el trabajo de autenticacion al controlador.
 * Las rutas son solo un mapa.
 */
public class AuthRoutes {
    
    // Creamos una instancia del motor de plantillas, asumiendo que se debe usar en cada ruta que devuelve ModelAndView
    private static final MustacheTemplateEngine engine = new MustacheTemplateEngine();
    
    public static void configure() {
        
        // 1. Inicializar la dependencia (Inyección de Dependencia Manual simple)
        UserRepository userRepository = new ActiveJDBCUserRepository();
        AuthController authController = new AuthController(userRepository);

        // GET / y /login: Muestra el formulario. El controlador devuelve un ModelAndView.
        // Spark requiere la sintaxis (URL, Route, TemplateEngine) para ModelAndView.
        get("/", authController::showLoginForm, engine);
        get("/login", authController::showLoginForm, engine); 

        // POST /login: 
        // Solución: Usamos un lambda explícito y hacemos un CAST explícito a ModelAndView.
        // Si el controlador hace res.redirect() y devuelve null, el cast no se realiza.
        post("/login", (req, res) -> {
            Object result = authController.login(req, res);
            
            // Si el resultado es una redirección (null), lo retornamos.
            if (result == null) {
                return null;
            }
            
            // Si el resultado es un error, Spark necesita que devolvamos un ModelAndView.
            // Aquí forzamos el tipo para satisfacer el compilador.
            return (ModelAndView) result;
        }, engine); 

        // GET /logout: Cierra la sesión y redirige. El controlador NO devuelve una vista,
        // sino que maneja la redirección internamente (res.redirect()), por lo que SÓLO necesita la URL y el Route.
        get("/logout", authController::logout);
    }
}