package com.uade.e_commerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uade.e_commerce.DTO.Pedido.PedidoRequestDTO;
import com.uade.e_commerce.DTO.Pedido.PedidoResponseDTO;
import com.uade.e_commerce.DTO.Pedido.PedidoResponseDTO.PedidoItemResponseDTO;
import com.uade.e_commerce.exception.ResourceNotFoundException;
import com.uade.e_commerce.model.Pedido;
import com.uade.e_commerce.model.PedidoItem;
import com.uade.e_commerce.model.Usuario;
import com.uade.e_commerce.repository.PedidoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PedidoService {

	private final PedidoRepository pedidoRepository;

	@PersistenceContext
	private EntityManager entityManager;

	public PedidoService(PedidoRepository pedidoRepository) {
		this.pedidoRepository = pedidoRepository;
	}

	public List<PedidoResponseDTO> getAllPedidos() {
		return pedidoRepository.findAll().stream()
				.map(this::toResponseDTO)
				.toList();
	}

	public PedidoResponseDTO getPedido(Long id) {
		return toResponseDTO(buscarPorId(id));
	}

	public List<PedidoResponseDTO> getPedidosByUsuario(Long usuarioId) {
		Usuario usuario = entityManager.find(Usuario.class, usuarioId);
		if (usuario == null) {
			throw new ResourceNotFoundException("Usuario no encontrado con id " + usuarioId);
		}

		return pedidoRepository.findByUsuario(usuario).stream()
				.map(this::toResponseDTO)
				.toList();
	}

	public PedidoResponseDTO actualizarEstado(Long id, PedidoRequestDTO dto) {
		Pedido pedido = buscarPorId(id);
		pedido.setEstadoPedido(dto.getEstadoPedido());
		return toResponseDTO(pedidoRepository.save(pedido));
	}

	private Pedido buscarPorId(Long id) {
		return pedidoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id " + id));
	}

	private PedidoResponseDTO toResponseDTO(Pedido pedido) {
		List<PedidoItemResponseDTO> items = pedido.getPedidoItems().stream()
				.map(this::toItemResponseDTO)
				.toList();

		return new PedidoResponseDTO(
				pedido.getId(),
				pedido.getUsuario() == null ? null : pedido.getUsuario().getId(),
				pedido.getFecha(),
				pedido.getMetodoPago(),
				pedido.getDireccionEnvio(),
				pedido.getTotal(),
				pedido.getEstadoPedido(),
				items);
	}

	private PedidoItemResponseDTO toItemResponseDTO(PedidoItem item) {
		return new PedidoItemResponseDTO(
				item.getId(),
				item.getProductoVariante() == null ? null : item.getProductoVariante().getId(),
				item.getNombreProducto(),
				item.getTallaVariante(),
				item.getColorVariante(),
				item.getPrecioUnitario(),
				item.getCantidad(),
				item.getSubtotal());
	}
}
