package com.uade.e_commerce.DTO.Usuario;

import com.uade.e_commerce.model.Genero;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

/**
 * Body del registro. Las validaciones acompanan a los nullable = false de la
 * entidad Usuario: sin ellas un body incompleto llegaba a la base y terminaba en
 * un 500 en vez de un 400 explicando que falta.
 *
 * Los @Pattern sobre los campos de texto libre son la defensa contra XSS almacenado:
 * la API guarda el texto tal cual y lo devuelve en los GET, asi que si entrara un
 * <script> quedaria persistido y se ejecutaria en cualquier front que lo renderice.
 */
@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "[^<>]*", message = "El nombre no puede contener los caracteres < o >")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Pattern(regexp = "[^<>]*", message = "El apellido no puede contener los caracteres < o >")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato valido")
    private String email;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
    private String contrasena;

    @Pattern(regexp = "[^<>]*", message = "El telefono no puede contener los caracteres < o >")
    private String telefono;

    private LocalDate fechaNacimiento;
    private Genero genero;

}
