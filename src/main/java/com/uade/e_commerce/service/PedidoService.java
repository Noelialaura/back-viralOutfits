package com.uade.e_commerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.uade.e_commerce.DTO.Pedido.EstadoPedidoRequestDTO;
import com.uade.e_commerce.DTO.Pedido.PedidoItemResponseDTO;
import com.uade.e_commerce.DTO.Pedido.PedidoRequestDTO;
import com.uade.e_commerce.DTO.Pedido.PedidoResponseDTO;
import com.uade.e_commerce.exception.BusinessRuleException;
import com.uade.e_commerce.exception.ResourceNotFoundException;
import com.uade.e_commerce.model.Carrito;
import com.uade.e_commerce.model.CarritoItem;
import com.uade.e_commerce.model.EstadoPedido;
import com.uade.e_commerce.model.Pedido;
import com.uade.e_commerce.model.PedidoItem;
import com.uade.e_commerce.model.VarianteProducto;
import com.uade.e_commerce.repository.PedidoRepository;
import com.uade.e_commerce.repository.UsuarioRepository;
import com.uade.e_commerce.repository.VarianteProductoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PedidoService {

	private final PedidoRepository pedidoRepository;
	private final CarritoService carritoService;
	private final VarianteProductoRepository varianteProductoRepository;
	private final UsuarioRepository usuarioRepository;

	public PedidoService(PedidoRepository pedidoRepository,
			CarritoService carritoService,
			VarianteProductoRepository varianteProductoRepository,
			UsuarioRepository usuarioRepository) {
		this.pedidoRepository = pedidoRepository;
		this.carritoService = carritoService;
		this.varianteProductoRepository = varianteProductoRepository;
		this.usuarioRepository = usuarioRepository;
	}

	public PedidoResponseDTO crearPedido(Long usuarioId, PedidoRequestDTO dto) {
		Carrito carrito = carritoService.obtenerOCrearCarrito(usuarioId);

		if (carrito.getCarritoItems().isEmpty()) {
			throw new BusinessRuleException("El carrito esta vacio, no se puede confirmar la compra");
		}

		Pedido pedido = new Pedido();
		pedido.setUsuario(carrito.getUsuario());
		pedido.setFecha(LocalDateTime.now());
		pedido.setMetodoPago(dto.getMetodoPago());
		pedido.setDireccionEnvio(dto.getDireccionEnvio());
		pedido.setEstadoPedido(EstadoPedido.PENDIENTE);

		double total = 0;

		for (CarritoItem carritoItem : carrito.getCarritoItems()) {
			VarianteProducto variante = carritoItem.getVarianteProducto();
			if (carritoItem.getCantidad() > variante.getStock()) {
				throw new BusinessRuleException("Stock insuficiente para " + variante.getProducto().getNombre()
						+ ". Stock disponible: " + variante.getStock());
			}

			double precioUnitario = variante.getProducto().getPrecio();
			double subtotal = precioUnitario * carritoItem.getCantidad();

			PedidoItem pedidoItem = new PedidoItem();
			pedidoItem.setPedido(pedido);
			pedidoItem.setVarianteProducto(variante);
			pedidoItem.setCantidad(carritoItem.getCantidad());
			pedidoItem.setPrecioUnitario(precioUnitario);
			pedidoItem.setSubtotal(subtotal);
			pedidoItem.setNombreProducto(variante.getProducto().getNombre());
			pedidoItem.setColorVariante(variante.getColor());
			pedidoItem.setTallaVariante(variante.getTalle());
			pedido.getPedidoItems().add(pedidoItem);

			variante.setStock(variante.getStock() - carritoItem.getCantidad());
			varianteProductoRepository.save(variante);

			total += subtotal;
		}

		pedido.setTotal(total);
		Pedido pedidoGuardado = pedidoRepository.save(pedido);

		carritoService.vaciarCarrito(usuarioId);

		return toResponseDTO(pedidoGuardado);
	}

	public List<PedidoResponseDTO> getPedidos(EstadoPedido estado) {
		List<Pedido> pedidos = (estado == null)
				? pedidoRepository.findAll()
				: pedidoRepository.findByEstadoPedido(estado);

		return pedidos.stream()
				.map(this::toResponseDTO)
				.toList();
	}

	public PedidoResponseDTO getPedido(Long id) {
		return toResponseDTO(buscarPorId(id));
	}

	public List<PedidoResponseDTO> getPedidosByUsuario(Long usuarioId) {
		if (!usuarioRepository.existsById(usuarioId)) {
			throw new ResourceNotFoundException("Usuario no encontrado con id " + usuarioId);
		}

		return pedidoRepository.findByUsuarioId(usuarioId).stream()
				.map(this::toResponseDTO)
				.toList();
	}

	public PedidoResponseDTO actualizarEstado(Long id, EstadoPedidoRequestDTO dto) {
		Pedido pedido = buscarPorId(id);
		pedido.setEstadoPedido(dto.getEstadoPedido());
		return toResponseDTO(pedidoRepository.save(pedido));
	}

	public void eliminarPedido(Long id) {
		Pedido pedido = buscarPorId(id);

		for (PedidoItem item : pedido.getPedidoItems()) {
			VarianteProducto variante = item.getVarianteProducto();
			if (variante != null) {
				variante.setStock(variante.getStock() + item.getCantidad());
				varianteProductoRepository.save(variante);
			}
		}

		pedidoRepository.delete(pedido);
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
				item.getVarianteProducto() == null ? null : item.getVarianteProducto().getId(),
				item.getNombreProducto(),
				item.getTallaVariante(),
				item.getColorVariante(),
				item.getPrecioUnitario(),
				item.getCantidad(),
				item.getSubtotal());
	}
}
