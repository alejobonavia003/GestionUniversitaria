package com.is1.proyecto.repositories;

import com.is1.proyecto.models.User;
import com.is1.proyecto.utils.LoggerUtil;

import java.util.Optional;
import org.slf4j.Logger;

/**
 * Esta clase es la que interactura con el modelo y la base de datos de los usuarios 
 * podes buscar usuarios por nombre o por nombre y contraceña
 */
public class ActiveJDBCUserRepository implements UserRepository {

private static final Logger logger = LoggerUtil.getLogger(ActiveJDBCUserRepository.class);

/**
 * @return retorna un usuario con todas sus propiedades o si no lo encuetra null
 */
    @Override
    public Optional<User> findByUsernameAndPassword(String username, String hashedPassword) {
        logger.debug("Buscando usuario: {}...", username);
        
        // ActiveJDBC: encuentra el primero que cumpla la condición.
        User user = User.findFirst("name = ? and password = ?", username, hashedPassword);
        
        if (user == null) {
            logger.warn("Intento de inicio de sesión fallido para el usuario: {}", username);
        } else {
            logger.info("Usuario '{}' autenticado con éxito. Rol: {}", user.getName(), user.getRole());
        }

        // Devolvemos un Optional
        return Optional.ofNullable(user);
    }
    
    /**
     * @return retorna un usuario con sus propiedades o null
     */
    @Override
    public Optional<User> findUserByUsername(String username) {
        // Implementación simple para buscar solo por nombre
        User user = User.findFirst("name = ?", username);
        return Optional.ofNullable(user);
    }

@Override
    public void save(User user) {
        // .saveIt() de ActiveJDBC maneja tanto la inserción (INSERT) 
        // como la actualización (UPDATE) automáticamente.
        user.saveIt();
    }
}
