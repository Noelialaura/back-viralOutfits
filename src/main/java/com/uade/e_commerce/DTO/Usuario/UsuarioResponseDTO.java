package com.uade.e_commerce.DTO.Usuario;

import java.time.LocalDate;

import com.uade.e_commerce.model.RolUsuario;
import com.uade.e_commerce.model.Genero;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioResponseDTO {
      private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;
    private Genero genero;
    private RolUsuario rol;
}
