package com.uade.e_commerce.DTO.Categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "[^<>]*", message = "El nombre no puede contener los caracteres < o >")
    private String nombre;

    @Pattern(regexp = "[^<>]*", message = "La descripcion no puede contener los caracteres < o >")
    private String descripcion;
}
