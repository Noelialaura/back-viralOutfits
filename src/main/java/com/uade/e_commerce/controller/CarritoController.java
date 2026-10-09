package com.uade.e_commerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<CarritoResponseDTO> obtenerCarrito(@AuthenticationPrincipal UsuarioDetails usuarioDetails) {
        CarritoResponseDTO carrito = carritoService.obtenerCarritoPorUsuario(UsuarioAutenticado.obtenerId(usuarioDetails));
        return ResponseEntity.status(HttpStatus.OK).body(carrito);
    }

    @PostMapping("/items")
    public ResponseEntity<CarritoResponseDTO> agregarItem(@AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @Valid @RequestBody CarritoItemRequestDTO dto) {
        CarritoResponseDTO carrito = carritoService.agregarItem(UsuarioAutenticado.obtenerId(usuarioDetails), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(carrito);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CarritoResponseDTO> actualizarCantidad(@AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @PathVariable Long itemId,
            @Valid @RequestBody CarritoItemUpdateDTO dto) {
        CarritoResponseDTO carrito = carritoService.actualizarCantidadItem(UsuarioAutenticado.obtenerId(usuarioDetails), itemId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(carrito);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CarritoResponseDTO> eliminarItem(@AuthenticationPrincipal UsuarioDetails usuarioDetails,
            @PathVariable Long itemId) {
        CarritoResponseDTO carrito = carritoService.eliminarItem(UsuarioAutenticado.obtenerId(usuarioDetails), itemId);
        return ResponseEntity.status(HttpStatus.OK).body(carrito);
    }

    @DeleteMapping
    public ResponseEntity<CarritoResponseDTO> vaciarCarrito(@AuthenticationPrincipal UsuarioDetails usuarioDetails) {
        CarritoResponseDTO carrito = carritoService.vaciarCarrito(UsuarioAutenticado.obtenerId(usuarioDetails));
        return ResponseEntity.status(HttpStatus.OK).body(carrito);
    }
}
