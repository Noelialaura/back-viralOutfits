package com.uade.e_commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.e_commerce.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByCategoriaId(Long categoriaId);

    List<Producto> findAllByOrderByNombreAsc();

    List<Producto> findByCategoriaIdOrderByNombreAsc(Long categoriaId);
}
