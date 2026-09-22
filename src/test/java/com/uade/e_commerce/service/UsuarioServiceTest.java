package com.uade.e_commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uade.e_commerce.DTO.Usuario.UsuarioRequestDTO;
import com.uade.e_commerce.DTO.Usuario.UsuarioResponseDTO;
import com.uade.e_commerce.model.Genero;
import com.uade.e_commerce.model.RolUsuario;
import com.uade.e_commerce.model.Usuario;
import com.uade.e_commerce.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void actualizarUsuario_debeActualizarCampos() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Ana");
        usuario.setApellido("García");
        usuario.setEmail("ana@mail.com");
        usuario.setContrasena("123456");
        usuario.setTelefono("1111");
        usuario.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        usuario.setGenero(Genero.FEMENINO);
        usuario.setRol(RolUsuario.CLIENTE);

        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNombre("Ana Maria");
        dto.setApellido("Pérez");
        dto.setEmail("anamaria@mail.com");
        dto.setTelefono("2222");
        dto.setFechaNacimiento(LocalDate.of(2001, 2, 2));
        dto.setGenero(Genero.FEMENINO);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioResponseDTO response = usuarioService.actualizarUsuario(1L, dto);

        assertEquals("Ana Maria", response.getNombre());
        assertEquals("Pérez", response.getApellido());
        assertEquals("anamaria@mail.com", response.getEmail());
        assertEquals("2222", response.getTelefono());
        assertEquals(Genero.FEMENINO, response.getGenero());
    }
}
