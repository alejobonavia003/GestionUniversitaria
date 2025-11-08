package com.is1.proyecto.services;

import com.is1.proyecto.integration.IntegrationTestBase; // <-- 1. Hereda de la clase base
import com.is1.proyecto.dto.LoginDTO;
import com.is1.proyecto.models.User;
import com.is1.proyecto.repositories.ActiveJDBCUserRepository;
import com.is1.proyecto.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de INTEGRACIÓN para AuthService.
 * Este test SÍ se conecta a la base de datos de prueba.
 */
public class AuthServiceTest extends IntegrationTestBase { // <-- 1. Hereda

    private AuthService authService;
    private UserRepository userRepository;

    private final String TEST_USER = "test_admin";
    private final String TEST_PASS_PLAIN = "Password123";

    @BeforeEach
    public void setUp() {
        // 2. Instanciamos las clases reales (No Mocks)
        userRepository = new ActiveJDBCUserRepository();
        authService = new AuthService(userRepository); // Asumiendo que AuthService usa DI

        // 3. Creamos un usuario real en la DB de prueba ANTES de cada test
        // (IntegrationTestBase ya limpió la DB)
        String HASHED_PASS = BCrypt.hashpw(TEST_PASS_PLAIN, BCrypt.gensalt());
        
        User testUser = new User();
        testUser.set("name", TEST_USER);
        testUser.set("password", HASHED_PASS);
        testUser.set("role", "ADMIN");
        testUser.saveIt(); // Guarda el usuario en la DB de prueba
    }

    @Test
    public void testAuthenticate_Success() {
        // ARRANGE
        LoginDTO loginDTO = new LoginDTO(TEST_USER, TEST_PASS_PLAIN);

        // ACT
        // El servicio ahora consulta la DB de prueba real
        Optional<User> result = authService.authenticate(loginDTO);

        // ASSERT
        assertTrue(result.isPresent(), "La autenticación debe ser exitosa.");
        assertEquals(TEST_USER, result.get().getString("name"), "El nombre de usuario debe coincidir.");
    }

    @Test
    public void testAuthenticate_Failure_IncorrectPassword() {
        // ARRANGE
        LoginDTO loginDTO = new LoginDTO(TEST_USER, "ContrasenaIncorrecta");

        // ACT
        Optional<User> result = authService.authenticate(loginDTO);

        // ASSERT
        assertFalse(result.isPresent(), "La autenticación debe fallar por contraseña incorrecta.");
    }

    @Test
    public void testAuthenticate_Failure_UserNotFound() {
        // ARRANGE
        LoginDTO loginDTO = new LoginDTO("usuario_inexistente", "CualquierPassword");

        // ACT
        Optional<User> result = authService.authenticate(loginDTO);

        // ASSERT
        assertFalse(result.isPresent(), "La autenticación debe fallar si el usuario no existe.");
    }
}