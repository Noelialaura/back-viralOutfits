package com.uade.e_commerce.DTO.Pedido;

import com.uade.e_commerce.model.EstadoPedido;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Body del cambio de estado (PUT /api/pedidos/{id}/estado). El campo se llama
 * estadoPedido y no estado para no cambiar el JSON que ya consume Postman.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoPedidoRequestDTO {

    @NotNull(message = "El estado del pedido es obligatorio")
    private EstadoPedido estadoPedido;
}
