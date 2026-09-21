package com.uade.e_commerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Presentacion concreta de un producto: la combinacion de talle y color que
 * realmente se vende, con su propio stock.
 *
 * No tiene el lado inverso hacia PedidoItem a proposito: PedidoItem guarda una
 * copia de los datos del producto al momento de la compra, no una relacion viva,
 * y esa lista no la consumia ningun service.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "VarianteProducto")
public class VarianteProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String talle;

    @Column(nullable = false, length = 50)
    private String color;

    @Column(nullable = false)
    private int stock;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
}
