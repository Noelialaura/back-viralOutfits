package com.uade.e_commerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.e_commerce.DTO.Pedido.EstadoPedidoRequestDTO;
import com.uade.e_commerce.DTO.Pedido.PedidoRequestDTO;
import com.uade.e_commerce.DTO.Pedido.PedidoResponseDTO;
import com.uade.e_commerce.model.EstadoPedido;
import com.uade.e_commerce.security.UsuarioAutenticado;
import com.uade.e_commerce.security.UsuarioDetails;
import com.uade.e_commerce.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

	private final PedidoService pedidoService;

	public PedidoController(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	@PostMapping
	public ResponseEntity<PedidoResponseDTO> crearPedido(
        @AuthenticationPrincipal UsuarioDetails usuarioDetails,
        @Valid @RequestBody PedidoRequestDTO pedidoRequest) {

    	PedidoResponseDTO nuevoPedido = pedidoService.crearPedido(
            UsuarioAutenticado.obtenerId(usuarioDetails), 
            pedidoRequest
    );

    	return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
}

	// Con ?estado=PENDIENTE el ADMIN filtra el listado por estado.
	// http://localhost:8080/api/pedidos?estado=PENDIENTE
	@GetMapping
	public ResponseEntity<List<PedidoResponseDTO>> getPedidos(@RequestParam(required = false) EstadoPedido estado) {
		return ResponseEntity.status(HttpStatus.OK).body(pedidoService.getPedidos(estado));
	}

	@GetMapping("/mis-pedidos")
	public ResponseEntity<List<PedidoResponseDTO>> getMisPedidos(@AuthenticationPrincipal UsuarioDetails usuarioDetails) {
		return ResponseEntity.status(HttpStatus.OK).body(pedidoService.getPedidosByUsuario(UsuarioAutenticado.obtenerId(usuarioDetails)));
	}

	// El ADMIN consulta cualquier pedido; el CLIENTE solamente los suyos.
	@GetMapping("/{id}")
	public ResponseEntity<PedidoResponseDTO> getPedido(@AuthenticationPrincipal UsuarioDetails usuarioDetails,
			@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(pedidoService.getPedido(id, UsuarioAutenticado.obtenerId(usuarioDetails),
				UsuarioAutenticado.esAdmin(usuarioDetails)));
	}

	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<List<PedidoResponseDTO>> getPedidosByUsuario(@PathVariable Long usuarioId) {
		return ResponseEntity.status(HttpStatus.OK).body(pedidoService.getPedidosByUsuario(usuarioId));
	}

	@PutMapping("/{id}/estado")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<PedidoResponseDTO> actualizarEstado(
			@PathVariable Long id,
			@Valid @RequestBody EstadoPedidoRequestDTO pedidoRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(pedidoService.actualizarEstado(id, pedidoRequest));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
		pedidoService.eliminarPedido(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
