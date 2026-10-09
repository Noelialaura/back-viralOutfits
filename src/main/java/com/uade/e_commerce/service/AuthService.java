package com.uade.e_commerce.service;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.e_commerce.DTO.Usuario.LoginRequestDTO;
import com.uade.e_commerce.DTO.Usuario.LoginResponseDTO;
import com.uade.e_commerce.DTO.Usuario.UsuarioRequestDTO;
import com.uade.e_commerce.DTO.Usuario.UsuarioResponseDTO;
import com.uade.e_commerce.exception.ArgumentInvalidException;
import com.uade.e_commerce.exception.InvalidCredentialsException;
import com.uade.e_commerce.model.RolUsuario;
import com.uade.e_commerce.model.Usuario;
import com.uade.e_commerce.repository.UsuarioRepository;
import com.uade.e_commerce.security.JwtService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder contrasenaEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder contrasenaEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.contrasenaEncoder = contrasenaEncoder;
        this.jwtService = jwtService;
    }

    public UsuarioResponseDTO registrar(UsuarioRequestDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ArgumentInvalidException("El email ya se encuentra registrado");
        }

        // El DTO no valida la fecha, asi que si el JSON no la trae hay que cortar antes de
        // llamar a isAfter() o el registro revienta con NullPointerException (500).
        if (dto.getFechaNacimiento() != null && dto.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new ArgumentInvalidException("La fecha de nacimiento no puede ser futura");
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setContrasena(contrasenaEncoder.encode(dto.getContrasena()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setGenero(dto.getGenero());
        usuario.setRol(RolUsuario.CLIENTE);

        Usuario guardado = usuarioRepository.save(usuario);

        return convertirAResponseDTO(guardado);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {

        // Email inexistente y contraseña incorrecta devuelven el mismo 401 con el mismo
        // mensaje: si el email inexistente diera otro codigo, cualquiera podria averiguar
        // que cuentas estan registradas probando emails contra el login.
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Email o contraseña incorrectos"));

        if (!contrasenaEncoder.matches(dto.getContrasena(), usuario.getContrasena())) {
            throw new InvalidCredentialsException("Email o contraseña incorrectos");
        }

        String token = jwtService.generarToken(usuario.getId(), usuario.getEmail(), usuario.getRol());

        return new LoginResponseDTO("Login exitoso", token, convertirAResponseDTO(usuario));
    }

    private UsuarioResponseDTO convertirAResponseDTO(Usuario usuario) {

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getFechaNacimiento(),
                usuario.getGenero(),
                usuario.getRol()
        );
    }
}