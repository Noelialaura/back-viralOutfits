package com.uade.e_commerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.e_commerce.DTO.Pedido.PedidoRequestDTO;
import com.uade.e_commerce.DTO.Pedido.PedidoResponseDTO;
import com.uade.e_commerce.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

	private final PedidoService pedidoService;

	public PedidoController(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	@GetMapping
	public List<PedidoResponseDTO> getAllPedidos() {
		return pedidoService.getAllPedidos();
	}

	@GetMapping("/{id}")
	public PedidoResponseDTO getPedido(@PathVariable Long id) {
		return pedidoService.getPedido(id);
	}

	@GetMapping("/usuario/{usuarioId}")
	public List<PedidoResponseDTO> getPedidosByUsuario(@PathVariable Long usuarioId) {
		return pedidoService.getPedidosByUsuario(usuarioId);
	}

	@PutMapping("/{id}/estado")
	@ResponseStatus(HttpStatus.OK)
	public PedidoResponseDTO actualizarEstado(
			@PathVariable Long id,
			@Valid @RequestBody PedidoRequestDTO pedidoRequest) {
		return pedidoService.actualizarEstado(id, pedidoRequest);
	}
}
