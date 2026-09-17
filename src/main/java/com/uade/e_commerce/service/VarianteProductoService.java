package com.uade.e_commerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uade.e_commerce.DTO.VarianteProducto.VarianteProductoRequestDTO;
import com.uade.e_commerce.DTO.VarianteProducto.VarianteProductoResponseDTO;
import com.uade.e_commerce.DTO.VarianteProducto.VarianteStockRequestDTO;
import com.uade.e_commerce.exception.BusinessRuleException;
import com.uade.e_commerce.exception.ResourceNotFoundException;
import com.uade.e_commerce.model.Producto;
import com.uade.e_commerce.model.VarianteProducto;
import com.uade.e_commerce.repository.CarritoItemRepository;
import com.uade.e_commerce.repository.PedidoItemRepository;
import com.uade.e_commerce.repository.ProductoRepository;
import com.uade.e_commerce.repository.VarianteProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VarianteProductoService {

    private final VarianteProductoRepository varianteProductoRepository;
    private final ProductoRepository productoRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final CarritoItemRepository carritoItemRepository;

    public VarianteProductoService(VarianteProductoRepository varianteProductoRepository,
            ProductoRepository productoRepository,
            PedidoItemRepository pedidoItemRepository,
            CarritoItemRepository carritoItemRepository) {
        this.varianteProductoRepository = varianteProductoRepository;
        this.productoRepository = productoRepository;
        this.pedidoItemRepository = pedidoItemRepository;
        this.carritoItemRepository = carritoItemRepository;
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

        if (varianteProductoRepository.existsByProductoIdAndTalleAndColor(productoId, dto.getTalle(), dto.getColor())) {
            throw new BusinessRuleException(
                    "El producto ya tiene una variante " + dto.getTalle() + " / " + dto.getColor());
        }

        VarianteProducto variante = new VarianteProducto();
        variante.setTalle(dto.getTalle());
        variante.setColor(dto.getColor());
        variante.setStock(dto.getStock());
        variante.setProducto(producto);

        return toResponseDTO(varianteProductoRepository.save(variante));
    }

    public VarianteProductoResponseDTO getVariante(Long productoId, Long varianteId) {
        return toResponseDTO(buscarVarianteDeProducto(productoId, varianteId));
    }

    public VarianteProductoResponseDTO actualizarVariante(Long productoId, Long varianteId, VarianteProductoRequestDTO dto) {
        VarianteProducto variante = buscarVarianteDeProducto(productoId, varianteId);

        if (varianteProductoRepository.existsByProductoIdAndTalleAndColorAndIdNot(
                productoId, dto.getTalle(), dto.getColor(), varianteId)) {
            throw new BusinessRuleException(
                    "El producto ya tiene una variante " + dto.getTalle() + " / " + dto.getColor());
        }

        variante.setTalle(dto.getTalle());
        variante.setColor(dto.getColor());
        variante.setStock(dto.getStock());

        return toResponseDTO(varianteProductoRepository.save(variante));
    }

    public VarianteProductoResponseDTO actualizarStock(Long productoId, Long varianteId, VarianteStockRequestDTO dto) {
        VarianteProducto variante = buscarVarianteDeProducto(productoId, varianteId);
        variante.setStock(dto.getStock());

        return toResponseDTO(varianteProductoRepository.save(variante));
    }

    public void eliminarVariante(Long productoId, Long varianteId) {
        VarianteProducto variante = buscarVarianteDeProducto(productoId, varianteId);
        if (pedidoItemRepository.existsByVarianteProductoId(varianteId)) {
            throw new BusinessRuleException(
                    "No se puede eliminar la variante porque forma parte de pedidos ya realizados");
        }

        // El pedido es la restriccion mas fuerte, asi que su mensaje tiene prioridad
        // cuando aplican los dos.
        long enCarritos = carritoItemRepository.countByVarianteProductoId(varianteId);
        if (enCarritos > 0) {
            throw new BusinessRuleException(
                    "No se puede eliminar la variante porque esta en el carrito de " + enCarritos + " cliente(s)");
        }

        varianteProductoRepository.delete(variante);
    }

    private VarianteProducto buscarVarianteDeProducto(Long productoId, Long varianteId) {
        VarianteProducto variante = varianteProductoRepository.findById(varianteId)
                .orElseThrow(() -> new ResourceNotFoundException("Variante no encontrada con id " + varianteId));

        if (!variante.getProducto().getId().equals(productoId)) {
            throw new ResourceNotFoundException("La variante " + varianteId + " no pertenece al producto " + productoId);
        }

        return variante;
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
