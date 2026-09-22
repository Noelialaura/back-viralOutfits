package com.uade.e_commerce.DTO.Pedido;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCrearRequestDTO {

    @NotBlank(message = "El metodo de pago es obligatorio")
    private String metodoPago;

    @NotBlank(message = "La direccion de envio es obligatoria")
    private String direccionEnvio;
}
