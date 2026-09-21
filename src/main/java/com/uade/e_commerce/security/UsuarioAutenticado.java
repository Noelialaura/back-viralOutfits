package com.uade.e_commerce.security;

import com.uade.e_commerce.exception.InvalidCredentialsException;

/**
 * Los endpoints de /api/** siguen abiertos en SecurityConfig para poder probar el
 * catalogo con Postman sin token. Como consecuencia, en los endpoints que si
 * necesitan saber quien es el usuario (carrito y pedidos) el principal llega en
 * null cuando no se manda el header Authorization.
 *
 * Este helper centraliza esa validacion y lanza InvalidCredentialsException, que
 * el GlobalExceptionHandler ya traduce a un 401 con el mismo formato JSON que el
 * resto de los errores de la API.
 */
public final class UsuarioAutenticado {

    private UsuarioAutenticado() {
    }

    public static Long obtenerId(UsuarioDetails usuarioDetails) {
        if (usuarioDetails == null) {
            throw new InvalidCredentialsException("Debe iniciar sesion para realizar esta operacion");
        }
        return usuarioDetails.getId();
    }
}
