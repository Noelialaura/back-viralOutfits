package com.uade.e_commerce.DTO.Producto;

import java.util.List;

import com.uade.e_commerce.DTO.Categoria.CategoriaResponseDTO;
import com.uade.e_commerce.DTO.VarianteProducto.VarianteProductoResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String marca;
    private CategoriaResponseDTO categoria;
    private List<VarianteProductoResponseDTO> variantes;
}
