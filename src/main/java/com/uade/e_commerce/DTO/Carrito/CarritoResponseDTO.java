package com.uade.e_commerce.DTO.Carrito;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoResponseDTO {
    private Long id;
    private Long usuarioId;
    private LocalDateTime fechaCreacion;
    private List<CarritoItemResponseDTO> items;
    private Double total;
}