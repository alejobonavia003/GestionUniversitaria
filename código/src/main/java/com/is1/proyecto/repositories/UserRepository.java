package com.is1.proyecto.repositories;

import com.is1.proyecto.models.User;
import java.util.Optional;


/**
 * Esta es la interface que uso para definir el repositorio de usuarios
 */
public interface UserRepository {
    // Definimos el contrato: buscar un usuario por nombre y contraseña.
    // Usamos Optional<User> para indicar que el usuario podría no existir.
    Optional<User> findByUsernameAndPassword(String username, String hashedPassword);
    
    // Contrato para buscar solo por nombre (útil para verificar existencia o roles)
    Optional<User> findUserByUsername(String username);
}