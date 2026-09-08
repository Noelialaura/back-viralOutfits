package com.uade.e_commerce.model;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table (name = "PedidoItem")
@Data

public class PedidoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Pedido pedido;
    @ManyToOne
    private VarianteProducto productoVariante;
    private int cantidad;
    private double precioUnitario;
    
}
