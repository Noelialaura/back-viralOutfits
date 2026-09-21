package com.uade.e_commerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.e_commerce.DTO.Producto.ProductoRequestDTO;
import com.uade.e_commerce.DTO.Producto.ProductoResponseDTO;
import com.uade.e_commerce.service.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoResponseDTO> getProductos(@RequestParam(required = false) Long categoriaId) {
        return productoService.getProductos(categoriaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponseDTO crearProducto(@Valid @RequestBody ProductoRequestDTO producto) {
        return productoService.crearProducto(producto);
    }

    @GetMapping("/{id}")
    public ProductoResponseDTO getProductoById(@PathVariable Long id) {
        return productoService.getProducto(id);
    }

    @PutMapping("/{id}")
    public ProductoResponseDTO actualizarProducto(@PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO producto) {
        return productoService.actualizarProducto(id, producto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }

}
