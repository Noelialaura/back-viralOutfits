package com.uade.e_commerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uade.e_commerce.DTO.VarianteProducto.VarianteProductoRequestDTO;
import com.uade.e_commerce.DTO.VarianteProducto.VarianteProductoResponseDTO;
import com.uade.e_commerce.exception.ResourceNotFoundException;
import com.uade.e_commerce.model.Producto;
import com.uade.e_commerce.model.VarianteProducto;
import com.uade.e_commerce.repository.ProductoRepository;
import com.uade.e_commerce.repository.VarianteProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VarianteProductoService {

    private final VarianteProductoRepository varianteProductoRepository;
    private final ProductoRepository productoRepository;

    public VarianteProductoService(VarianteProductoRepository varianteProductoRepository, ProductoRepository productoRepository) {
        this.varianteProductoRepository = varianteProductoRepository;
        this.productoRepository = productoRepository;
    }

    public List<VarianteProductoResponseDTO> getVariantesDeProducto(Long productoId) {
        asegurarQueProductoExiste(productoId);
        return varianteProductoRepository.findByProductoId(productoId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public VarianteProductoResponseDTO agregarVariante(Long productoId, VarianteProductoRequestDTO dto) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + productoId));

        VarianteProducto variante = new VarianteProducto();
        variante.setTalle(dto.getTalle());
        variante.setColor(dto.getColor());
        variante.setStock(dto.getStock());
        variante.setProducto(producto);

        return toResponseDTO(varianteProductoRepository.save(variante));
    }

    public void eliminarVariante(Long productoId, Long varianteId) {
        VarianteProducto variante = varianteProductoRepository.findById(varianteId)
                .orElseThrow(() -> new ResourceNotFoundException("Variante no encontrada con id " + varianteId));

        if (!variante.getProducto().getId().equals(productoId)) {
            throw new ResourceNotFoundException("La variante " + varianteId + " no pertenece al producto " + productoId);
        }

        varianteProductoRepository.delete(variante);
    }

    private void asegurarQueProductoExiste(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new ResourceNotFoundException("Producto no encontrado con id " + productoId);
        }
    }

    private VarianteProductoResponseDTO toResponseDTO(VarianteProducto variante) {
        return new VarianteProductoResponseDTO(variante.getId(), variante.getTalle(), variante.getColor(), variante.getStock());
    }
}
