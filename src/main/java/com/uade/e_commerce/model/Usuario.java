package com.uade.e_commerce.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de dominio pura. La adaptacion a Spring Security vive en
 * {@code com.uade.e_commerce.security.UsuarioDetails}, que es lo que usa
 * CustomUserDetailsService: antes esta clase tambien implementaba UserDetails y
 * quedaban dos implementaciones distintas del mismo contrato.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    @Column(unique = true, nullable = false)
    private String email;
    private String contrasena;
    private String telefono;
    private LocalDate fechaNacimiento;
    @Enumerated(EnumType.STRING)
    private RolUsuario rol;
    @Enumerated(EnumType.STRING)
    private Genero genero;

    // Excluidos de toString/equals: Carrito y Pedido apuntan de vuelta a Usuario y las
    // dos clases usan @Data, asi que la recursion terminaba en StackOverflowError.
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Carrito carrito;

    @OneToMany(mappedBy = "usuario")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Pedido> pedidos = new ArrayList<>();
}
