package com.uade.e_commerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.e_commerce.DTO.Carrito.CarritoItemUpdateDTO;
import com.uade.e_commerce.DTO.Carrito.CarritoItemRequestDTO;
import com.uade.e_commerce.DTO.Carrito.CarritoResponseDTO;
import com.uade.e_commerce.security.UsuarioAutenticado;
import com.uade.e_commerce.security.UsuarioDetails;
import com.uade.e_commerce.service.CarritoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public CarritoResponseDTO obtenerCarrito(@AuthenticationPrincipal UsuarioDetails usuarioDetails) {
        return carritoService.obtenerCarritoPorUsuario(UsuarioAutenticado.obtenerId(usuarioDetails));
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CarritoResponseDTO agregarItem(@AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @Valid @RequestBody CarritoItemRequestDTO dto) {
        return carritoService.agregarItem(UsuarioAutenticado.obtenerId(usuarioDetails), dto);
    }

    @PutMapping("/items/{itemId}")
    public CarritoResponseDTO actualizarCantidad(@AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @PathVariable Long itemId,
            @Valid @RequestBody CarritoItemUpdateDTO dto) {
        return carritoService.actualizarCantidadItem(UsuarioAutenticado.obtenerId(usuarioDetails), itemId, dto);
    }

    @DeleteMapping("/items/{itemId}")
    public CarritoResponseDTO eliminarItem(@AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @PathVariable Long itemId) {
        return carritoService.eliminarItem(UsuarioAutenticado.obtenerId(usuarioDetails), itemId);
    }

    @DeleteMapping
    public CarritoResponseDTO vaciarCarrito(@AuthenticationPrincipal UsuarioDetails usuarioDetails) {
        return carritoService.vaciarCarrito(UsuarioAutenticado.obtenerId(usuarioDetails));
    }
}
