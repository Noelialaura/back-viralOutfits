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

import com.uade.e_commerce.DTO.VarianteProducto.VarianteProductoRequestDTO;
import com.uade.e_commerce.DTO.VarianteProducto.VarianteProductoResponseDTO;
import com.uade.e_commerce.DTO.VarianteProducto.VarianteStockRequestDTO;
import com.uade.e_commerce.service.VarianteProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos/{productoId}/variantes")
public class VarianteProductoController {

    private final VarianteProductoService varianteProductoService;

    VarianteProductoController(VarianteProductoService varianteProductoService) {
        this.varianteProductoService = varianteProductoService;
    }

    @GetMapping
    public List<VarianteProductoResponseDTO> getVariantes(@PathVariable Long productoId) {
        return varianteProductoService.getVariantesDeProducto(productoId);
    }

    @GetMapping("/{varianteId}")
    public VarianteProductoResponseDTO getVariante(@PathVariable Long productoId,
            @PathVariable Long varianteId) {
        return varianteProductoService.getVariante(productoId, varianteId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VarianteProductoResponseDTO agregarVariante(@PathVariable Long productoId,
            @Valid @RequestBody VarianteProductoRequestDTO variante) {
        return varianteProductoService.agregarVariante(productoId, variante);
    }

    @PutMapping("/{varianteId}")
    public VarianteProductoResponseDTO actualizarVariante(@PathVariable Long productoId,
            @PathVariable Long varianteId,
            @Valid @RequestBody VarianteProductoRequestDTO variante) {
        return varianteProductoService.actualizarVariante(productoId, varianteId, variante);
    }

    @PutMapping("/{varianteId}/stock")
    public VarianteProductoResponseDTO actualizarStock(@PathVariable Long productoId,
            @PathVariable Long varianteId,
            @Valid @RequestBody VarianteStockRequestDTO stock) {
        return varianteProductoService.actualizarStock(productoId, varianteId, stock);
    }

    @DeleteMapping("/{varianteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarVariante(@PathVariable Long productoId, @PathVariable Long varianteId) {
        varianteProductoService.eliminarVariante(productoId, varianteId);
    }
}
