package com.uade.e_commerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.uade.e_commerce.DTO.Carrito.CarritoRequestDTO;
import com.uade.e_commerce.DTO.Carrito.CarritoItemResponseDTO;
import com.uade.e_commerce.DTO.Carrito.CarritoResponseDTO;
import com.uade.e_commerce.exception.BusinessRuleException;
import com.uade.e_commerce.exception.ResourceNotFoundException;
import com.uade.e_commerce.model.Carrito;
import com.uade.e_commerce.model.CarritoItem;
import com.uade.e_commerce.model.Usuario;
import com.uade.e_commerce.model.VarianteProducto;
import com.uade.e_commerce.repository.CarritoRepository;
import com.uade.e_commerce.repository.UsuarioRepository;
import com.uade.e_commerce.repository.VarianteProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final VarianteProductoRepository varianteProductoRepository;

    public CarritoService(CarritoRepository carritoRepository,
                          UsuarioRepository usuarioRepository,
                          VarianteProductoRepository varianteProductoRepository) {
        this.carritoRepository = carritoRepository;
        this.usuarioRepository = usuarioRepository;
        this.varianteProductoRepository = varianteProductoRepository;
    }

    public CarritoResponseDTO obtenerCarritoPorUsuario(Long usuarioId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        return toResponseDTO(carrito);
    }

    public CarritoResponseDTO agregarItem(Long usuarioId, CarritoRequestDTO dto) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        VarianteProducto variante = varianteProductoRepository.findById(dto.getVarianteProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Variante no encontrada con id " + dto.getVarianteProductoId()));

        if (carrito.getCarritoItems() == null) {
            carrito.setCarritoItems(new ArrayList<>());
        }

        // Buscar si ya estÃ¡ en el carrito
        Optional<CarritoItem> itemExistente = carrito.getCarritoItems().stream()
                .filter(item -> item.getVarianteProducto().getId().equals(variante.getId()))
                .findFirst();

        int cantidadFinal = dto.getCantidad();
        if (itemExistente.isPresent()) {
            cantidadFinal += itemExistente.get().getCantidad();
        }

        // Validar stock disponible
        if (cantidadFinal > variante.getStock()) {
            throw new BusinessRuleException("Stock insuficiente para el producto seleccionado. Stock disponible: " + variante.getStock());
        }

        if (itemExistente.isPresent()) {
            itemExistente.get().setCantidad(cantidadFinal);
        } else {
            CarritoItem nuevoItem = new CarritoItem();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setVarianteProducto(variante);
            nuevoItem.setCantidad(dto.getCantidad());
            carrito.getCarritoItems().add(nuevoItem);
        }

        return toResponseDTO(carritoRepository.save(carrito));
    }

    public CarritoResponseDTO eliminarItem(Long usuarioId, Long itemId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        boolean eliminado = carrito.getCarritoItems().removeIf(item -> item.getId().equals(itemId));
        if (!eliminado) {
            throw new ResourceNotFoundException("El item con id " + itemId + " no se encuentra en el carrito");
        }

        return toResponseDTO(carritoRepository.save(carrito));
    }

    public CarritoResponseDTO vaciarCarrito(Long usuarioId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        carrito.getCarritoItems().clear();
        return toResponseDTO(carritoRepository.save(carrito));
    }

    public Carrito obtenerOCrearCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId).orElseGet(() -> {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + usuarioId));
            Carrito nuevoCarrito = new Carrito();
            nuevoCarrito.setUsuario(usuario);
            nuevoCarrito.setFechaCreacion(LocalDateTime.now());
            nuevoCarrito.setCarritoItems(new ArrayList<>());
            return carritoRepository.save(nuevoCarrito);
        });
    }

    private CarritoResponseDTO toResponseDTO(Carrito carrito) {
        List<CarritoItemResponseDTO> itemsDTO = carrito.getCarritoItems().stream().map(item -> {
            VarianteProducto v = item.getVarianteProducto();
            double precio = v.getProducto().getPrecio();
            double subtotal = precio * item.getCantidad();

            return new CarritoItemResponseDTO(
                    item.getId(),
                    v.getId(),
                    v.getProducto().getNombre(),
                    v.getTalle(),
                    v.getColor(),
                    precio,
                    item.getCantidad(),
                    subtotal
            );
        }).toList();

        double total = itemsDTO.stream()
                .mapToDouble(CarritoItemResponseDTO::getSubtotal)
                .sum();

        return new CarritoResponseDTO(
                carrito.getId(),
                carrito.getUsuario().getId(),
                carrito.getFechaCreacion(),
                itemsDTO,
                total
        );
    }
}