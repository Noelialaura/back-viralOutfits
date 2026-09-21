package com.uade.e_commerce.DTO.VarianteProducto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Body del endpoint de manejo de stock, para poder actualizar solamente el stock
 * de una variante sin tener que reenviar talle y color.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VarianteStockRequestDTO {

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;
}
