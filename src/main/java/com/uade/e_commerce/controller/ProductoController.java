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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.e_commerce.DTO.Producto.ProductoRequestDTO;
import com.uade.e_commerce.DTO.Producto.ProductoResponseDTO;
import com.uade.e_commerce.service.ProductoService;

import jakarta.validation.Valid;

// http://localhost:8080/api/productos
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Endpoint 1 : Obtener el catálogo de ropa
    // http://localhost:8080/api/productos
    @GetMapping()
    public List<ProductoResponseDTO> getAllProductos() {
        return productoService.getAllProductos();
    }

    // Endpoint 2 : Registrar/cargar nueva prenda
    // http://localhost:8080/api/productos
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponseDTO crearProducto(@Valid @RequestBody ProductoRequestDTO producto) {
        return productoService.crearProducto(producto);
    }

    // Endpoint 3 : obtener un producto segun id
    // get http://localhost:8080/api/productos/1
    @GetMapping("/{id}")
    public ProductoResponseDTO getProductoById(@PathVariable Long id) {
        return productoService.getProducto(id);
    }

    // Endpoint 4 : actualizar un producto segun id
    // put http://localhost:8080/api/productos/1
    @PutMapping("/{id}")
    public ProductoResponseDTO actualizarProducto(@PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO producto) {
        return productoService.actualizarProducto(id, producto);
    }

    // Endpoint 5 : eliminar un producto segun id
    // delete http://localhost:8080/api/productos/1
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }

}
