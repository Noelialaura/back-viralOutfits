package com.uade.e_commerce.DTO.Pedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItemResponseDTO {

    private Long id;
    private Long varianteProductoId;
    private String productoNombre;
    private String talle;
    private String color;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
