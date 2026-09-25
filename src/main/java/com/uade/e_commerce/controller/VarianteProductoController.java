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
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<VarianteProductoResponseDTO>> getVariantes(@PathVariable Long productoId) {
        return ResponseEntity.status(HttpStatus.OK).body(varianteProductoService.getVariantesDeProducto(productoId));
    }

    @GetMapping("/{varianteId}")
    public ResponseEntity<VarianteProductoResponseDTO> getVariante(@PathVariable Long productoId,
            @PathVariable Long varianteId) {
        return ResponseEntity.status(HttpStatus.OK).body(varianteProductoService.getVariante(productoId, varianteId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VarianteProductoResponseDTO> agregarVariante(@PathVariable Long productoId,
            @Valid @RequestBody VarianteProductoRequestDTO variante) {
        return ResponseEntity.status(HttpStatus.CREATED).body(varianteProductoService.agregarVariante(productoId, variante));
    }

    @PutMapping("/{varianteId}")
    public ResponseEntity<VarianteProductoResponseDTO> actualizarVariante(
        @PathVariable Long productoId,
        @PathVariable Long varianteId,
        @Valid @RequestBody VarianteProductoRequestDTO variante) {

        VarianteProductoResponseDTO actualizada = varianteProductoService.actualizarVariante(productoId, varianteId, variante);

        return ResponseEntity.status(HttpStatus.OK).body(actualizada);
}

    @PutMapping("/{varianteId}/stock")
    public ResponseEntity<VarianteProductoResponseDTO> actualizarStock(
        @PathVariable Long productoId,
        @PathVariable Long varianteId,
        @Valid @RequestBody VarianteStockRequestDTO stock) {

        VarianteProductoResponseDTO actualizada = varianteProductoService.actualizarStock(productoId, varianteId, stock);

            return ResponseEntity.status(HttpStatus.OK).body(actualizada);
}

    @DeleteMapping("/{varianteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> eliminarVariante(@PathVariable Long productoId, @PathVariable Long varianteId) {
        varianteProductoService.eliminarVariante(productoId, varianteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
