package com.uade.e_commerce.model;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Renglon de un pedido. Los campos nombreProducto, colorVariante, tallaVariante y
 * precioUnitario son una copia de los datos al momento de la compra: si despues
 * cambia el precio o el nombre de la publicacion, el pedido sigue mostrando lo que
 * el cliente realmente compro.
 */
@Entity
@Table (name = "PedidoItem")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PedidoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variante_producto_id")
    private VarianteProducto varianteProducto;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private double precioUnitario;

    @Column(nullable = false)
    private double subtotal;

    @Column(nullable = false, length = 150)
    private String nombreProducto;

    @Column(length = 50)
    private String colorVariante;

    @Column(length = 20)
    private String tallaVariante;
}
