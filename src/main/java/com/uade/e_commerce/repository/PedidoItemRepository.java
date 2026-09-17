package com.uade.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.e_commerce.model.PedidoItem;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {

    boolean existsByVarianteProductoId(Long varianteProductoId);

    boolean existsByVarianteProductoProductoId(Long productoId);
}
