package com.uade.e_commerce.DTO.VarianteProducto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VarianteProductoResponseDTO {

    private Long id;
    private String talle;
    private String color;
    private int stock;
}
