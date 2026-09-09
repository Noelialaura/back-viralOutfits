package com.uade.e_commerce.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.uade.e_commerce.model.RolUsuario;
import com.uade.e_commerce.model.Usuario;
import com.uade.e_commerce.repository.UsuarioRepository;

/**
 * El auto-registro de POST /auth/registro siempre crea un CLIENTE, asi que en una base
 * recien creada no existiria ningun ADMIN y no habria forma de probar los endpoints que
 * se restrinjan por rol sin insertar el usuario a mano por SQL. Este runner crea el
 * ADMIN inicial la primera vez que arranca la aplicacion.
 */
@Configuration
public class AdminInicialConfig {

    private static final String ADMIN_EMAIL = "admin@uade.com";
    private static final String ADMIN_CONTRASENA = "admin123";

    @Bean
    public CommandLineRunner crearAdminInicial(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.existsByEmail(ADMIN_EMAIL)) {
                return;
            }

            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setApellido("UADE");
            admin.setEmail(ADMIN_EMAIL);
            admin.setContrasena(passwordEncoder.encode(ADMIN_CONTRASENA));
            admin.setRol(RolUsuario.ADMIN);

            usuarioRepository.save(admin);
            System.out.println("Usuario ADMIN inicial creado: " + ADMIN_EMAIL + " / " + ADMIN_CONTRASENA);
        };
    }
}
