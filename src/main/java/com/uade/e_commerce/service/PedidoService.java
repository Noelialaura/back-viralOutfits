package com.uade.e_commerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uade.e_commerce.DTO.Pedido.PedidoCrearRequestDTO;
import com.uade.e_commerce.DTO.Pedido.PedidoRequestDTO;
import com.uade.e_commerce.DTO.Pedido.PedidoResponseDTO;
import com.uade.e_commerce.DTO.Pedido.PedidoResponseDTO.PedidoItemResponseDTO;
import com.uade.e_commerce.exception.BusinessRuleException;
import com.uade.e_commerce.exception.ResourceNotFoundException;
import com.uade.e_commerce.model.Carrito;
import com.uade.e_commerce.model.CarritoItem;
import com.uade.e_commerce.model.EstadoPedido;
import com.uade.e_commerce.model.Pedido;
import com.uade.e_commerce.model.PedidoItem;
import com.uade.e_commerce.model.Usuario;
import com.uade.e_commerce.repository.CarritoRepository;
import com.uade.e_commerce.repository.PedidoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PedidoService {

	private final PedidoRepository pedidoRepository;
	private final CarritoRepository carritoRepository;

	@PersistenceContext
	private EntityManager entityManager;

	public PedidoService(PedidoRepository pedidoRepository, CarritoRepository carritoRepository) {
		this.pedidoRepository = pedidoRepository;
		this.carritoRepository = carritoRepository;
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

	public PedidoResponseDTO crearPedido(Long usuarioId, PedidoCrearRequestDTO dto) {
		Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
				.orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado para el usuario " + usuarioId));

		if (carrito.getCarritoItems() == null || carrito.getCarritoItems().isEmpty()) {
			throw new BusinessRuleException("No se puede crear un pedido con el carrito vacio");
		}

		Pedido pedido = new Pedido();
		pedido.setUsuario(carrito.getUsuario());
		pedido.setFecha(java.time.LocalDateTime.now());
		pedido.setMetodoPago(dto.getMetodoPago());
		pedido.setDireccionEnvio(dto.getDireccionEnvio());
		pedido.setEstadoPedido(EstadoPedido.PENDIENTE);

		double total = 0;
		for (CarritoItem carritoItem : carrito.getCarritoItems()) {
			varianteProductoDisponible(carritoItem);

			varianteProducto(carritoItem).setStock(
					varianteProducto(carritoItem).getStock() - carritoItem.getCantidad());

			PedidoItem pedidoItem = new PedidoItem();
			pedidoItem.setPedido(pedido);
			pedidoItem.setProductoVariante(varianteProducto(carritoItem));
			pedidoItem.setCantidad(carritoItem.getCantidad());
			pedidoItem.setPrecioUnitario(varianteProducto(carritoItem).getProducto().getPrecio());
			pedidoItem.setSubtotal(pedidoItem.getPrecioUnitario() * pedidoItem.getCantidad());
			pedidoItem.setNombreProducto(varianteProducto(carritoItem).getProducto().getNombre());
			pedidoItem.setTallaVariante(varianteProducto(carritoItem).getTalle());
			pedidoItem.setColorVariante(varianteProducto(carritoItem).getColor());
			pedido.getPedidoItems().add(pedidoItem);
			total += pedidoItem.getSubtotal();
		}

		pedido.setTotal(total);
		carrito.getCarritoItems().clear();
		carritoRepository.save(carrito);
		return toResponseDTO(pedidoRepository.save(pedido));
	}

	private void varianteProductoDisponible(CarritoItem carritoItem) {
		if (carritoItem.getCantidad() > varianteProducto(carritoItem).getStock()) {
			throw new BusinessRuleException("Stock insuficiente para el producto seleccionado");
		}
	}

	private com.uade.e_commerce.model.VarianteProducto varianteProducto(CarritoItem carritoItem) {
		return carritoItem.getVarianteProducto();
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
