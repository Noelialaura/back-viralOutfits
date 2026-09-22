package com.uade.e_commerce.service;

import org.springframework.stereotype.Service;

import com.uade.e_commerce.DTO.Usuario.UsuarioRequestDTO;
import com.uade.e_commerce.DTO.Usuario.UsuarioResponseDTO;
import com.uade.e_commerce.exception.ResourceNotFoundException;
import com.uade.e_commerce.model.Usuario;
import com.uade.e_commerce.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponseDTO buscarPorId(Long id) {

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        return convertirAResponseDTO(usuario);
    }

    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        if (dto.getNombre() != null) {
            usuario.setNombre(dto.getNombre());
        }
        if (dto.getApellido() != null) {
            usuario.setApellido(dto.getApellido());
        }
        if (dto.getEmail() != null) {
            usuario.setEmail(dto.getEmail());
        }
        if (dto.getTelefono() != null) {
            usuario.setTelefono(dto.getTelefono());
        }
        if (dto.getFechaNacimiento() != null) {
            usuario.setFechaNacimiento(dto.getFechaNacimiento());
        }
        if (dto.getGenero() != null) {
            usuario.setGenero(dto.getGenero());
        }

        return convertirAResponseDTO(usuarioRepository.save(usuario));
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