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

import com.uade.e_commerce.DTO.VarianteProducto.VarianteProductoRequestDTO;
import com.uade.e_commerce.DTO.VarianteProducto.VarianteProductoResponseDTO;
import com.uade.e_commerce.model.Producto;
import com.uade.e_commerce.model.VarianteProducto;
import com.uade.e_commerce.repository.ProductoRepository;
import com.uade.e_commerce.repository.VarianteProductoRepository;

@ExtendWith(MockitoExtension.class)
class VarianteProductoServiceTest {

    @Mock
    private VarianteProductoRepository varianteProductoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private VarianteProductoService varianteProductoService;

    @Test
    void actualizarVariante_debeActualizarCampos() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Remera");
        producto.setDescripcion("Remera prueba");
        producto.setPrecio(100.0);
        producto.setMarca("Marca");

        VarianteProducto variante = new VarianteProducto();
        variante.setId(2L);
        variante.setTalle("M");
        variante.setColor("Azul");
        variante.setStock(12);
        variante.setProducto(producto);

        VarianteProductoRequestDTO dto = new VarianteProductoRequestDTO();
        dto.setTalle("L");
        dto.setColor("Negro");
        dto.setStock(20);

        when(varianteProductoRepository.findById(2L)).thenReturn(Optional.of(variante));
        when(varianteProductoRepository.save(any(VarianteProducto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VarianteProductoResponseDTO response = varianteProductoService.actualizarVariante(1L, 2L, dto);

        assertEquals("L", response.getTalle());
        assertEquals("Negro", response.getColor());
        assertEquals(20, response.getStock());
    }
}
