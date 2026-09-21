package com.uade.e_commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.e_commerce.model.VarianteProducto;

public interface VarianteProductoRepository extends JpaRepository<VarianteProducto, Long> {

    List<VarianteProducto> findByProductoId(Long productoId);

    boolean existsByProductoIdAndTalleAndColor(Long productoId, String talle, String color);

    boolean existsByProductoIdAndTalleAndColorAndIdNot(Long productoId, String talle, String color, Long id);
}
