package com.uade.e_commerce.repository;

import com.uade.e_commerce.model.EstadoPedido;
import com.uade.e_commerce.model.Pedido;
import com.uade.e_commerce.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuario(Usuario usuario);

    List<Pedido> findByEstadoPedido(EstadoPedido estadoPedido);

    List<Pedido> findByUsuarioAndEstadoPedido(Usuario usuario, EstadoPedido estadoPedido);

    List<Pedido> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);
}
