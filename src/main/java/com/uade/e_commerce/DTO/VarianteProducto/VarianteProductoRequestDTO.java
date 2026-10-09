package com.uade.e_commerce.DTO.VarianteProducto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VarianteProductoRequestDTO {

    // Talle y color son lo que define a la variante y lo que usa el control de
    // duplicados, asi que no pueden venir vacios.
    @NotBlank(message = "El talle es obligatorio")
    @Pattern(regexp = "[^<>]*", message = "El talle no puede contener los caracteres < o >")
    private String talle;

    @NotBlank(message = "El color es obligatorio")
    @Pattern(regexp = "[^<>]*", message = "El color no puede contener los caracteres < o >")
    private String color;

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;
}
