package com.is1.proyecto.services;

import com.is1.proyecto.dto.LoginDTO;
import com.is1.proyecto.models.User;
import com.is1.proyecto.repositories.UserRepository;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
/**
 * esta clase es parte de la logica de negocio
 * 
 * En esta clase voy a escribir todas las operaciones o metodos que voy nesesitando para tratar con los usuarios
 * 
 */

public class AuthService {

    //creo un atributo para traer un repositorio de usuarios
    private final UserRepository userRepository;

    //constructor acarreo el repositorio desde la ruta
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 
     * @param loginDTO
     * @return si el usuario esta autenticado retorna el usuario sino retorna vacio
     */
    public Optional<User> authenticate(LoginDTO loginDTO) {

        Optional<User> userOptional = userRepository.findUserByUsername(loginDTO.getUsername());

       if (userOptional.isPresent()) {
            User user = userOptional.get();
            String hashedDbPassword = user.getString("password");
            
            // VERIFICACIÓN CON BCRYPt en el Service
            if (BCrypt.checkpw(loginDTO.getPassword(), hashedDbPassword)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
