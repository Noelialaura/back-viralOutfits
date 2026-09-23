package com.uade.e_commerce.Config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.uade.e_commerce.model.Categoria;
import com.uade.e_commerce.repository.CategoriaRepository;

/**
 * Las categorias del e-commerce son fijas: Calzado, Ropa y Accesorios. Al ser datos del
 * negocio y no datos de prueba, se siembran al arrancar en vez de crearse desde Postman,
 * asi el catalogo ya tiene sus categorias reales la primera vez que se levanta la app.
 *
 * El ABM de categorias sigue disponible para el ADMIN: esto solo garantiza el punto de
 * partida.
 */
@Configuration
public class CategoriaInicialConfig {

    private record CategoriaInicial(String nombre, String descripcion) {
    }

    private static final List<CategoriaInicial> CATEGORIAS = List.of(
            new CategoriaInicial("Calzado", "Zapatillas, botas y sandalias"),
            new CategoriaInicial("Ropa", "Remeras, buzos, pantalones y camperas"),
            new CategoriaInicial("Accesorios", "Gorras, mochilas, cinturones y medias"));

    @Bean
    public CommandLineRunner crearCategoriasIniciales(CategoriaRepository categoriaRepository) {
        return args -> {
            for (CategoriaInicial inicial : CATEGORIAS) {
                // El chequeo evita duplicar en cada reinicio: la columna nombre es unique,
                // asi que sin esto el segundo arranque reventaria al guardar.
                if (categoriaRepository.existsByNombre(inicial.nombre())) {
                    continue;
                }

                Categoria categoria = new Categoria();
                categoria.setNombre(inicial.nombre());
                categoria.setDescripcion(inicial.descripcion());
                categoriaRepository.save(categoria);
            }
        };
    }
}
