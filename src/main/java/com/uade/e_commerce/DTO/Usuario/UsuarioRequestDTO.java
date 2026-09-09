package com.uade.e_commerce.DTO.Usuario;
import com.uade.e_commerce.model.Genero;
import java.time.LocalDate;
import lombok.Data;
@Data 
public class UsuarioRequestDTO {
    private String nombre;
    private String apellido;
    private String email;
    private String contrasena;
    private String telefono;
    private LocalDate fechaNacimiento;
    private Genero genero;

}
