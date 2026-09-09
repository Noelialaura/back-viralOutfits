package com.uade.e_commerce.DTO.Pedido;

import java.time.LocalDateTime;
import java.util.List;

import com.uade.e_commerce.model.EstadoPedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {

	private Long id;
	private Long usuarioId;
	private LocalDateTime fecha;
	private String metodoPago;
	private String direccionEnvio;
	private Double total;
	private EstadoPedido estadoPedido;
	private List<PedidoItemResponseDTO> items;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PedidoItemResponseDTO {
		private Long id;
		private Long productoVarianteId;
		private String nombreProducto;
		private String talla;
		private String color;
		private Double precioUnitario;
		private Integer cantidad;
		private Double subtotal;
	}
}
