package com.uade.e_commerce.security;

import com.uade.e_commerce.exception.InvalidCredentialsException;

/**
 * Helpers para leer el usuario que viene en el token, sin que los controllers tengan
 * que manipular el SecurityContext a mano.
 *
 * SecurityConfig ya exige token en carrito, pedidos y usuarios, asi que el principal
 * normalmente no llega en null. La validacion de obtenerId() queda igual como red de
 * seguridad: lanza InvalidCredentialsException, que el GlobalExceptionHandler traduce
 * a un 401 con el mismo formato JSON que el resto de los errores de la API.
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

    public static boolean esAdmin(UsuarioDetails usuarioDetails) {
        return usuarioDetails != null && usuarioDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
