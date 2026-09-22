package com.uade.e_commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uade.e_commerce.DTO.Producto.ProductoRequestDTO;
import com.uade.e_commerce.DTO.Producto.ProductoResponseDTO;
import com.uade.e_commerce.model.Categoria;
import com.uade.e_commerce.model.Producto;
import com.uade.e_commerce.repository.CategoriaRepository;
import com.uade.e_commerce.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void actualizarProducto_debeActualizarCamposYCategoria() {
        Categoria categoria = new Categoria();
        categoria.setId(10L);
        categoria.setNombre("Ropa");
        categoria.setDescripcion("Prendas");

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Remera vieja");
        producto.setDescripcion("Vieja");
        producto.setPrecio(100.0);
        producto.setMarca("Marca vieja");
        producto.setCategoria(categoria);

        ProductoRequestDTO dto = new ProductoRequestDTO();
        dto.setNombre("Remera nueva");
        dto.setDescripcion("Nueva descripcion");
        dto.setPrecio(250.0);
        dto.setMarca("Marca nueva");
        dto.setCategoriaId(10L);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(categoriaRepository.findById(10L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductoResponseDTO response = productoService.actualizarProducto(1L, dto);

        assertEquals("Remera nueva", response.getNombre());
        assertEquals("Nueva descripcion", response.getDescripcion());
        assertEquals(250.0, response.getPrecio());
        assertEquals("Marca nueva", response.getMarca());
        assertEquals(10L, response.getCategoria().getId());
    }
}
