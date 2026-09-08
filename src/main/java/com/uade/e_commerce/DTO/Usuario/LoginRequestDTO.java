package com.uade.e_commerce.DTO.Usuario;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String email;
    private String contrasena;
}