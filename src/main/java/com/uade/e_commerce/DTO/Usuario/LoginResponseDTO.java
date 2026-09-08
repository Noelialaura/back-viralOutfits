package com.uade.e_commerce.DTO.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {

    private String mensaje;
    private UsuarioResponseDTO usuario;
}