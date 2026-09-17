package com.uade.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.e_commerce.model.CarritoItem;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    long countByVarianteProductoId(Long varianteProductoId);

    boolean existsByVarianteProductoProductoId(Long productoId);
}
