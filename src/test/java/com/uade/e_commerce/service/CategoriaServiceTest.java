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

import com.uade.e_commerce.DTO.Categoria.CategoriaRequestDTO;
import com.uade.e_commerce.DTO.Categoria.CategoriaResponseDTO;
import com.uade.e_commerce.model.Categoria;
import com.uade.e_commerce.repository.CategoriaRepository;
import com.uade.e_commerce.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void actualizarCategoria_debeActualizarCampos() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Ropa");
        categoria.setDescripcion("Categoria vieja");

        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNombre("Accesorios");
        dto.setDescripcion("Nueva descripcion");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponseDTO response = categoriaService.actualizarCategoria(1L, dto);

        assertEquals("Accesorios", response.getNombre());
        assertEquals("Nueva descripcion", response.getDescripcion());
    }
}
