package com.uade.e_commerce.DTO.Producto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "[^<>]*", message = "El nombre no puede contener los caracteres < o >")
    private String nombre;

    @Pattern(regexp = "[^<>]*", message = "La descripcion no puede contener los caracteres < o >")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private Double precio;

    @Pattern(regexp = "[^<>]*", message = "La marca no puede contener los caracteres < o >")
    private String marca;

    @NotNull(message = "La categoria es obligatoria")
    private Long categoriaId;
}
