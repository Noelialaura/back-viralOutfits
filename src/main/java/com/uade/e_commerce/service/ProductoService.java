package com.uade.e_commerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uade.e_commerce.DTO.Categoria.CategoriaResponseDTO;
import com.uade.e_commerce.DTO.Producto.ProductoRequestDTO;
import com.uade.e_commerce.DTO.Producto.ProductoResponseDTO;
import com.uade.e_commerce.DTO.VarianteProducto.VarianteProductoResponseDTO;
import com.uade.e_commerce.exception.ResourceNotFoundException;
import com.uade.e_commerce.model.Categoria;
import com.uade.e_commerce.model.Producto;
import com.uade.e_commerce.repository.CategoriaRepository;
import com.uade.e_commerce.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProductoResponseDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id " + dto.getCategoriaId()));

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setMarca(dto.getMarca());
        producto.setCategoria(categoria);

        return toResponseDTO(productoRepository.save(producto));
    }

    public ProductoResponseDTO getProducto(Long id) {
        return toResponseDTO(buscarPorId(id));
    }

    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO dto) {
        Producto producto = buscarPorId(id);
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id " + dto.getCategoriaId()));

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setMarca(dto.getMarca());
        producto.setCategoria(categoria);

        return toResponseDTO(productoRepository.save(producto));
    }

    public void eliminarProducto(Long id) {
        Producto producto = buscarPorId(id);
        productoRepository.delete(producto);
    }

    private Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
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
