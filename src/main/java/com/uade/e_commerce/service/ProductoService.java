package com.uade.e_commerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uade.e_commerce.DTO.Categoria.CategoriaResponseDTO;
import com.uade.e_commerce.DTO.Producto.ProductoRequestDTO;
import com.uade.e_commerce.DTO.Producto.ProductoResponseDTO;
import com.uade.e_commerce.DTO.VarianteProducto.VarianteProductoResponseDTO;
import com.uade.e_commerce.exception.BusinessRuleException;
import com.uade.e_commerce.exception.ResourceNotFoundException;
import com.uade.e_commerce.model.Categoria;
import com.uade.e_commerce.model.Producto;
import com.uade.e_commerce.repository.CarritoItemRepository;
import com.uade.e_commerce.repository.CategoriaRepository;
import com.uade.e_commerce.repository.PedidoItemRepository;
import com.uade.e_commerce.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final CarritoItemRepository carritoItemRepository;

    public ProductoService(ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            PedidoItemRepository pedidoItemRepository,
            CarritoItemRepository carritoItemRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.pedidoItemRepository = pedidoItemRepository;
        this.carritoItemRepository = carritoItemRepository;
    }

    public List<ProductoResponseDTO> getProductos(Long categoriaId) {
        List<Producto> productos;

        if (categoriaId == null) {
            productos = productoRepository.findAllByOrderByNombreAsc();
        } else {
            buscarCategoria(categoriaId);
            productos = productoRepository.findByCategoriaIdOrderByNombreAsc(categoriaId);
        }

        return productos.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setMarca(dto.getMarca());
        producto.setCategoria(buscarCategoria(dto.getCategoriaId()));

        return toResponseDTO(productoRepository.save(producto));
    }

    public ProductoResponseDTO getProducto(Long id) {
        return toResponseDTO(buscarPorId(id));
    }

    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO dto) {
        Producto producto = buscarPorId(id);
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setMarca(dto.getMarca());
        producto.setCategoria(buscarCategoria(dto.getCategoriaId()));

        return toResponseDTO(productoRepository.save(producto));
    }

    public void eliminarProducto(Long id) {
        Producto producto = buscarPorId(id);
        if (pedidoItemRepository.existsByVarianteProductoProductoId(id)) {
            throw new BusinessRuleException(
                    "No se puede eliminar el producto porque forma parte de pedidos ya realizados");
        }

        if (carritoItemRepository.existsByVarianteProductoProductoId(id)) {
            throw new BusinessRuleException(
                    "No se puede eliminar el producto porque alguna de sus variantes esta en el carrito de un cliente");
        }

        productoRepository.delete(producto);
    }

    private Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
    }

    private Categoria buscarCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id " + categoriaId));
    }

    private ProductoResponseDTO toResponseDTO(Producto producto) {
        CategoriaResponseDTO categoriaDTO = producto.getCategoria() == null ? null : new CategoriaResponseDTO(
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre(),
                producto.getCategoria().getDescripcion());

        List<VarianteProductoResponseDTO> variantesDTO = producto.getVariantes().stream()
                .map(v -> new VarianteProductoResponseDTO(v.getId(), v.getTalle(), v.getColor(), v.getStock()))
                .toList();

        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getMarca(),
                categoriaDTO,
                variantesDTO);
    }
}
