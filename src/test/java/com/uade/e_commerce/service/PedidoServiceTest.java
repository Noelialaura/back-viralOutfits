package com.uade.e_commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uade.e_commerce.DTO.Pedido.PedidoCrearRequestDTO;
import com.uade.e_commerce.DTO.Pedido.PedidoResponseDTO;
import com.uade.e_commerce.model.Carrito;
import com.uade.e_commerce.model.CarritoItem;
import com.uade.e_commerce.model.EstadoPedido;
import com.uade.e_commerce.model.Producto;
import com.uade.e_commerce.model.Usuario;
import com.uade.e_commerce.model.VarianteProducto;
import com.uade.e_commerce.repository.CarritoRepository;
import com.uade.e_commerce.repository.PedidoRepository;
import com.uade.e_commerce.repository.VarianteProductoRepository;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private VarianteProductoRepository varianteProductoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void crearPedido_debeCopiarCarritoDescontarStockYVaciarCarrito() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Producto producto = new Producto();
        producto.setId(2L);
        producto.setNombre("Remera");
        producto.setPrecio(100.0);

        VarianteProducto variante = new VarianteProducto();
        variante.setId(3L);
        variante.setTalle("M");
        variante.setColor("Azul");
        variante.setStock(10);
        variante.setProducto(producto);

        Carrito carrito = new Carrito();
        carrito.setId(4L);
        carrito.setUsuario(usuario);
        CarritoItem carritoItem = new CarritoItem();
        carritoItem.setId(5L);
        carritoItem.setCarrito(carrito);
        carritoItem.setVarianteProducto(variante);
        carritoItem.setCantidad(2);
        carrito.setCarritoItems(new ArrayList<>(List.of(carritoItem)));

        PedidoCrearRequestDTO dto = new PedidoCrearRequestDTO("Tarjeta", "Calle 123");

        when(carritoRepository.findByUsuarioId(1L)).thenReturn(Optional.of(carrito));
        when(pedidoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PedidoResponseDTO response = pedidoService.crearPedido(1L, dto);

        assertEquals(EstadoPedido.PENDIENTE, response.getEstadoPedido());
        assertEquals(200.0, response.getTotal());
        assertEquals(1, response.getItems().size());
        assertEquals(8, variante.getStock());
        assertEquals(0, carrito.getCarritoItems().size());
    }
}
