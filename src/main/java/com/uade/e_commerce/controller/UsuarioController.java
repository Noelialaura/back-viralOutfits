package com.uade.e_commerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.e_commerce.DTO.Usuario.UsuarioResponseDTO;
import com.uade.e_commerce.DTO.Usuario.UsuarioUpdateDTO;
import com.uade.e_commerce.security.UsuarioAutenticado;
import com.uade.e_commerce.security.UsuarioDetails;
import com.uade.e_commerce.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.listarUsuarios());
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> perfilPropio(@AuthenticationPrincipal UsuarioDetails usuarioDetails) {
        Long usuarioId = UsuarioAutenticado.obtenerId(usuarioDetails);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarPorId(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDTO usuario) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.actualizar(id, usuario));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
