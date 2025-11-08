package com.is1.proyecto.integration;

import org.javalite.activejdbc.Base;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class IntegrationTestBase {

    // Lista de tablas en orden inverso de dependencia para un borrado seguro.
    private static final String[] TABLES_TO_CLEAN = {
        "Rindio", "Cursa", "Dicta", "Correlativa", "Materia", 
        "Vigente", "PlanEstudio", "Carrera", "Estudiante", 
        "Profesor", "Persona", "users"
    };

    @BeforeEach
    public void setupActiveJDBC() {
        // 1. Abre la conexión a la base de datos de prueba.
        // Se añade un timeout para evitar errores de "database is locked".
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:./db/universidad_test.db?busy_timeout=5000", "user", "pass");
        
        // 2. Inicia una transacción para asegurar que todas las operaciones de borrado se completen.
        Base.openTransaction();

        try {
            // 3. Desactiva temporalmente las claves foráneas para permitir el borrado.
            // Esto es una salvaguarda por si el orden no fuera perfecto.
            Base.exec("PRAGMA foreign_keys = OFF;");

            // 4. Borra los datos de TODAS TRTODSAASS PAAPA las tablas en el orden definido.
            for (String table : TABLES_TO_CLEAN) {
                Base.exec("DELETE FROM " + table);
            }

            // 5. Reactiva las claves foráneas para que los tests se ejecuten en un estado normal.
            Base.exec("PRAGMA foreign_keys = ON;");

            // 6. Confirma la transacción si todo fue bien.
            Base.commitTransaction();
        } catch (Exception e) {
            // Si algo falla, deshace la transacción para no dejar la DB en un estado inconsistente.
            Base.rollbackTransaction();
            throw new RuntimeException("Fallo al limpiar la base de datos de prueba.", e);
        }
    }

    @AfterEach
    public void tearDownActiveJDBC() {
        // Cierra la conexión después de cada test.
        Base.close();
    }
}