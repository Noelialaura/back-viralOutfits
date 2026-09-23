package com.uade.e_commerce.DTO.Pedido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Body del checkout (POST /api/pedidos). Los items no viajan en el request porque
 * salen del carrito del usuario autenticado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {

    @NotBlank(message = "El metodo de pago es obligatorio")
    @Pattern(regexp = "[^<>]*", message = "El metodo de pago no puede contener los caracteres < o >")
    private String metodoPago;

    @NotBlank(message = "La direccion de envio es obligatoria")
    @Pattern(regexp = "[^<>]*", message = "La direccion de envio no puede contener los caracteres < o >")
    private String direccionEnvio;
}
