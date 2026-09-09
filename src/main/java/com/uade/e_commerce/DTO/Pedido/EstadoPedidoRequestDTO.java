package com.uade.e_commerce.DTO.Pedido;

import com.uade.e_commerce.model.EstadoPedido;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoPedidoRequestDTO {

    @NotNull(message = "El estado es obligatorio")
    private EstadoPedido estado;
}
