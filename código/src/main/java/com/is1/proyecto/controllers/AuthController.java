package com.is1.proyecto.controllers;

import spark.Request;
import spark.Response;
import spark.ModelAndView;
import com.is1.proyecto.dto.LoginDTO;
import com.is1.proyecto.services.AuthService;
import com.is1.proyecto.models.User;
import com.is1.proyecto.repositories.UserRepository;
import com.is1.proyecto.utils.LoggerUtil;
import org.slf4j.Logger;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

public class AuthController {
    
    //voy a usar el repositorio de usuarios 
    private final UserRepository userRepository;
    private static final Logger logger = LoggerUtil.getLogger(AuthController.class);

    // Constructor
    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Este metodo lo uso siempre que quiera renderizar la pantalla de login.
     * la pantalla de login tiene una casilla para mensaje de error le podemos pasar algo al mensaje tambien 
     * @param req cuerpo de la peticion 
     * @param errorMessage por si queremos mostrar un mensaje sino quiero pongo este parametro en null
     * @return la renderizacion del modelo mustache
     */
    private ModelAndView getLoginModelAndView(Request req, String errorMessage, String successMessage) {
        Map<String, Object> model = new HashMap<>();

        //si la funcion se llamo con un mensaje de error lo cargo al modelo
        if (errorMessage != null && !errorMessage.isEmpty()) {
            // El mensaje de error se pasa al modelo de la vista
            model.put("errorMessage", errorMessage); 
        }

        //si llego con un mensaje bueno tambien lo cargoi al modelo
        if (errorMessage != null && !errorMessage.isEmpty()) {
            // El mensaje de successMessage se pasa al modelo de la vista
            model.put("successMessage", successMessage); 
        }
        //renderizo el modelo
        return new ModelAndView(model, "login.mustache");
    }

    /**
     * [Ruta: GET /login] Muestra el formulario de inicio de sesión.
     * Retorna ModelAndView para ser renderizado por Mustache.
     */
    public ModelAndView showLoginForm(Request req, Response res) {
        logger.info("Mostrando formulario de login.");
        return getLoginModelAndView(req, null, null); // Sin mensaje de error inicial
    }

    /**
     * [Ruta: POST /login] Procesa el formulario y autentica al usuario.
     * Retorna Object (ModelAndView o null después de redirect).
     */
    public Object login(Request req, Response res) {

        // este dto es el objeto que reprecenta a la secion con la que el usuario quiere iniciar secion
        LoginDTO loginData = mapRequestToLoginDTO(req); 
        
        // 1. Buscar al usuario SOLO por el nombre de usuario ESTO PREFIERO DELEGARLO AL SERVICIO ANQUE SEA BASICO
        // Optional<User> userOptional = userRepository.findUserByUsername(loginData.getUsername());

        //creo un servicio de autenticacion y me fijo si el usuario que cargo los datos esta auteticado
        AuthService authService = new AuthService(userRepository);
        Optional<User> userOptional = authService.authenticate(loginData);

        //si el objeto esta precente es que paso la autenticacion
        if (userOptional.isPresent()) {
            User user = userOptional.get(); //creo un usuario con el dto

                // Éxito en la autenticación
                req.session(true).attribute("loggedUserId", user.getId()); 
                req.session().attribute("userRole", user.getRole());
                req.session().attribute("name", user.getName());
                
                logger.info("Inicio de sesion exitoso. Redirigiendo a /dashboard. Rol: {}", user.getRole());
                res.redirect("/dashboard");
                return null; // Null indica a Spark que la redirección ha sido manejada
            
        } 
        
        // Fallo de Autenticación (Usuario no encontrado o Contraseña incorrecta)
        logger.warn("Fallo de autenticación para el usuario: {}", loginData.getUsername());
        
        // Retornamos el formulario de login con un mensaje de error
        return getLoginModelAndView(req, "Usuario o contraseña incorrectos.", null);
    }
    
    /**
     * [Ruta: GET /logout] Cierra la sesión del usuario.
     */
    public Object logout(Request req, Response res) {
        if (req.session(false) != null) {
            req.session().invalidate();
        }
        logger.info("Sesión cerrada. Redirigiendo a /login.");
        // Redirigimos a la ruta /login, no solo a /
        res.redirect("/login?message=Sesión cerrada correctamente."); 
        return null;
    }
    
    // --- IMPORTANTE: Métodos Auxiliares ---
    // Este método debe ser implementado. Asumimos que los parámetros se llaman 'username' y 'password' en el formulario.
    private LoginDTO mapRequestToLoginDTO(Request req) {
        // En Spark, obtienes los datos del formulario así:
        return new LoginDTO(req.queryParams("username"), req.queryParams("password"));
    }
}