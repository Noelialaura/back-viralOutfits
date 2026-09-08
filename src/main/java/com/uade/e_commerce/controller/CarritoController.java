package com.uade.e_commerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.e_commerce.DTO.Carrito.CarritoRequestDTO;
import com.uade.e_commerce.DTO.Carrito.CarritoResponseDTO;
import com.uade.e_commerce.service.CarritoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping("/{usuarioId}")
    public CarritoResponseDTO obtenerCarrito(@PathVariable Long usuarioId) {
        return carritoService.obtenerCarritoPorUsuario(usuarioId);
    }

    @PostMapping("/{usuarioId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CarritoResponseDTO agregarItem(@PathVariable Long usuarioId,
                                          @Valid @RequestBody CarritoRequestDTO dto) {
        return carritoService.agregarItem(usuarioId, dto);
    }

    @DeleteMapping("/{usuarioId}/items/{itemId}")
    public CarritoResponseDTO eliminarItem(@PathVariable Long usuarioId,
                                           @PathVariable Long itemId) {
        return carritoService.eliminarItem(usuarioId, itemId);
    }

    @DeleteMapping("/{usuarioId}")
    public CarritoResponseDTO vaciarCarrito(@PathVariable Long usuarioId) {
        return carritoService.vaciarCarrito(usuarioId);
    }
}