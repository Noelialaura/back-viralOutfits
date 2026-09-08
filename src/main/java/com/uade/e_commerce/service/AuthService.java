package com.uade.e_commerce.service;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.e_commerce.DTO.Usuario.LoginRequestDTO;
import com.uade.e_commerce.DTO.Usuario.LoginResponseDTO;
import com.uade.e_commerce.DTO.Usuario.UsuarioRequestDTO;
import com.uade.e_commerce.DTO.Usuario.UsuarioResponseDTO;
import com.uade.e_commerce.Exception.ArgumentInvalidException;
import com.uade.e_commerce.Exception.InvalidCredentialsException;
import com.uade.e_commerce.model.RolUsuario;
import com.uade.e_commerce.model.Usuario;
import com.uade.e_commerce.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder contrasenaEncoder;

    public AuthService(UsuarioRepository usuarioRepository,PasswordEncoder contrasenaEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.contrasenaEncoder = contrasenaEncoder;
    }

    public UsuarioResponseDTO registrar(UsuarioRequestDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ArgumentInvalidException("El email ya se encuentra registrado");
        }

        if (dto.getFechaNacimiento().isAfter(LocalDate.now())) {
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

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new ArgumentInvalidException( "Email o contraseña incorrectos"));

       if (!contrasenaEncoder.matches(dto.getContrasena(), usuario.getContrasena())) {

            throw new InvalidCredentialsException("Email o contraseña incorrectos");
        }

        return new LoginResponseDTO("Login exitoso",convertirAResponseDTO(usuario));
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