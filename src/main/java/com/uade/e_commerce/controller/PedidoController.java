package com.uade.e_commerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoResponseDTO crearPedido(@AuthenticationPrincipal UsuarioDetails usuarioDetails,
			@Valid @RequestBody PedidoRequestDTO pedidoRequest) {
		return pedidoService.crearPedido(UsuarioAutenticado.obtenerId(usuarioDetails), pedidoRequest);
	}

	// Con ?estado=PENDIENTE el ADMIN filtra el listado por estado.
	// http://localhost:8080/api/pedidos?estado=PENDIENTE
	@GetMapping
	public List<PedidoResponseDTO> getPedidos(@RequestParam(required = false) EstadoPedido estado) {
		return pedidoService.getPedidos(estado);
	}

	@GetMapping("/mis-pedidos")
	public List<PedidoResponseDTO> getMisPedidos(@AuthenticationPrincipal UsuarioDetails usuarioDetails) {
		return pedidoService.getPedidosByUsuario(UsuarioAutenticado.obtenerId(usuarioDetails));
	}

	// El ADMIN consulta cualquier pedido; el CLIENTE solamente los suyos.
	@GetMapping("/{id}")
	public PedidoResponseDTO getPedido(@AuthenticationPrincipal UsuarioDetails usuarioDetails,
			@PathVariable Long id) {
		return pedidoService.getPedido(id, UsuarioAutenticado.obtenerId(usuarioDetails),
				UsuarioAutenticado.esAdmin(usuarioDetails));
	}

	@GetMapping("/usuario/{usuarioId}")
	public List<PedidoResponseDTO> getPedidosByUsuario(@PathVariable Long usuarioId) {
		return pedidoService.getPedidosByUsuario(usuarioId);
	}

	@PutMapping("/{id}/estado")
	@ResponseStatus(HttpStatus.OK)
	public PedidoResponseDTO actualizarEstado(
			@PathVariable Long id,
			@Valid @RequestBody EstadoPedidoRequestDTO pedidoRequest) {
		return pedidoService.actualizarEstado(id, pedidoRequest);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminarPedido(@PathVariable Long id) {
		pedidoService.eliminarPedido(id);
	}
}
