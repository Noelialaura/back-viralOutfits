package com.uade.e_commerce.DTO.Carrito;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItemResponseDTO {
    private Long id;
    private Long varianteProductoId;
    private String nombreProducto;
    private String talle;
    private String color;
    private Double precioUnitario;
    private Integer cantidad;
    private Double subtotal;
}