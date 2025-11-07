package com.is1.proyecto.routes;

import static spark.Spark.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import com.is1.proyecto.models.persona.PersonaConcreta;
import com.is1.proyecto.models.persona.Profesor;

public class ProfesorRoutes {
    public static void configure() {
        get("/profesor", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            String currentUsername = req.session().attribute("currentUserUsername");
            Boolean loggedIn = req.session().attribute("loggedIn");
            if (currentUsername == null || loggedIn == null || !loggedIn) {
                System.out.println("DEBUG: Acceso no autorizado a /dashboard. Redirigiendo a /login.");
                res.redirect("?error=Debes iniciar sesion para acceder a esta pagina.");
                return null;
            }

            model.put("username", currentUsername);
            model.put("pageTitle", "Gestión de Profesores");
            return new ModelAndView(model, "profesor.mustache");
        }, new MustacheTemplateEngine());

        get("/alta-profesor", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            Boolean loggedIn = req.session().attribute("loggedIn");
            if (loggedIn == null || !loggedIn) {
                System.out.println("DEBUG: Acceso no autorizado a /dashboard. Redirigiendo a /login.");
                res.redirect("?error=Debes iniciar sesion para acceder a esta pagina.");
                return null;
            }

            String successMessage = req.queryParams("message");
            if (successMessage != null && !successMessage.isEmpty()) {
                System.out.println("DEBUGGG :::::::::::::::" + successMessage);
                model.put("successMessage", successMessage);
            }

            String errorMessage = req.queryParams("error");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                System.out.println("DEBUGGG :::::::::::::::" + errorMessage);
                model.put("errorMessage", errorMessage);
            }
            return new ModelAndView(model, "alta_profesor.mustache");
        }, new MustacheTemplateEngine());//

        get("/listar-profesores", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            
            Boolean loggedIn = req.session().attribute("loggedIn");
            if (loggedIn == null || !loggedIn) {
                res.redirect("/?error=Debes iniciar sesion para acceder a esta pagina.");
                return null;
            }

            List<Map<String, Object>> profesores = new ArrayList<>();
            for (Object obj : Profesor.findAll()) {
                Profesor profesor = (Profesor) obj;
                PersonaConcreta persona = PersonaConcreta.findFirst("dni = ?", profesor.getInteger("dni"));
                if (persona != null) {
                    Map<String, Object> profData = new HashMap<>();
                    profData.put("id", profesor.getId());
                    profData.put("nombre", persona.getString("nombre"));
                    profData.put("apellido", persona.getString("apellido"));
                    profData.put("dni", persona.getInteger("dni"));
                    profesores.add(profData);
                }
            }
            
            model.put("profesores", profesores);
            model.put("pageTitle", "Listado de Profesores");
            return new ModelAndView(model, "table_profesor.mustache");
        }, new MustacheTemplateEngine());

        post("/profesor", (req, res) -> {
            res.type("application/json");

            String name = req.queryParams("nombre");
            String apellido = req.queryParams("apellido");
            String dniS = req.queryParams("dni");
            String telefono = req.queryParams("telefono");
            String direccion = req.queryParams("direccion");
            String email = req.queryParams("email");

            if (name == null || name.isEmpty() || apellido == null || apellido.isEmpty() || email == null || email.isEmpty() 
                || dniS == null || dniS.isEmpty() || telefono == null || telefono.isEmpty() || direccion == null || direccion.isEmpty()) {
                res.redirect("/alta-profesor?error=Debes rellenar todos los campos");
            }

            Integer dni = Integer.parseInt(dniS);
            
            // Verificar si ya existe una persona con ese DNI
            PersonaConcreta personaExistente = PersonaConcreta.findFirst("dni = ?", dni);
            if (personaExistente != null) {
                // Si la persona existe, verificar si ya es profesor
                if (Profesor.findFirst("dni = ?", dni) != null) {
                    res.redirect("/alta-profesor?error=Esta persona ya está registrada como profesor!");
                    return "";
                }
            }

            // Verificar si el email ya está en uso
            PersonaConcreta emailExistente = PersonaConcreta.findFirst("email = ?", email);
            if (emailExistente != null) {
                res.redirect("/alta-profesor?error=El email ya está registrado!");
                return "";
            }

            try {
                // 1. Crear la persona
                PersonaConcreta persona = new PersonaConcreta();
                persona.setDni(dni);
                persona.setNombre(name);
                persona.setApellido(apellido);
                persona.setTelefono(telefono);
                persona.setDireccion(direccion);
                persona.setEmail(email);
                persona.saveIt();

                // 2. Verificar que la persona se guardó correctamente
                PersonaConcreta personaGuardada = PersonaConcreta.findFirst("dni = ?", dni);
                if (personaGuardada == null) {
                    throw new RuntimeException("Error al guardar la persona");
                }

                // 3. Crear el profesor con referencia a la persona
                Profesor profesor = new Profesor();
                profesor.set("dni", dni); // Establece la relación con la persona mediante el DNI
                profesor.saveIt();

                // 4. Verificar que el profesor se guardó correctamente
                if (!Profesor.existsByDni(dni)) {
                    // Si algo sale mal, eliminamos la persona para mantener consistencia
                    personaGuardada.delete();
                    throw new RuntimeException("Error al crear el profesor");
                }

                res.redirect("/alta-profesor?message=Profesor " + name + " agregado exitosamente!");
                return "";

            } catch (Exception e) {
                System.err.println("Error al registrar profesor: " + e.getMessage());
                e.printStackTrace();
                res.redirect("/alta-profesor?error=Error interno al crear el profesor. Intente de nuevo.");
                return "";
            }
        });

        get("/listar-profesores", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            Boolean loggedIn = req.session().attribute("loggedIn");
            if (loggedIn == null || !loggedIn) {
                System.out.println("DEBUG: Acceso no autorizado a /dashboard. Redirigiendo a /login.");
                res.redirect("?error=Debes iniciar sesion para acceder a esta pagina.");
                return null;
            }

            try {
                List<PersonaConcreta> profesores = PersonaConcreta.findAll();
                List<Map<String, Object>> listaProfesores = new ArrayList<>();

                for (PersonaConcreta p : profesores) {
                    Map<String, Object> profMap = new HashMap<>();
                    profMap.put("id", p.getDni());
                    profMap.put("nombre", p.getNombre());
                    profMap.put("apellido", p.getApellido());
                    profMap.put("dni", p.getDni());
                    listaProfesores.add(profMap);
                    System.out.println("DEBUG::::::::::::::::::     " + p.getInteger("dni"));
                }
                model.put("profesores", listaProfesores);

            } catch (Exception e) {
                System.err.println("Error al listar profesores: " + e.getMessage());
                res.redirect("/error?error=error al listar profesores");
            }

            return new ModelAndView(model, "table_profesor.mustache");
        }, new MustacheTemplateEngine());
    }
}