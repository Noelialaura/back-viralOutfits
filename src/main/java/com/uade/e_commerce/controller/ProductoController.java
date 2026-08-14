package com.uade.e_commerce.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.e_commerce.model.Producto;
import com.uade.e_commerce.service.ProductoService;



// http://localhost:8080/api/productos
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Endpoint 1 : Obtener el catálogo de ropa 
    @GetMapping()
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    // Endpoint 2 : Registrar/cargar nueva prenda 
    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoService.crearProducto(producto);
    }
}
