package com.uade.e_commerce.DTO.Pedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Renglon de la respuesta de un pedido. Los datos del producto salen de la copia
 * que PedidoItem guardo al momento de la compra, no de la publicacion actual.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItemResponseDTO {

    private Long id;
    private Long varianteProductoId;
    private String nombreProducto;
    private String talla;
    private String color;
    private Double precioUnitario;
    private Integer cantidad;
    private Double subtotal;
}
