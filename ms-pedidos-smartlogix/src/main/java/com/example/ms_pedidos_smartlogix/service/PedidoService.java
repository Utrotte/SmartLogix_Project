package com.example.ms_pedidos_smartlogix.service;

import com.example.ms_pedidos_smartlogix.dto.PedidoRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.PedidoResponseDTO;
import com.example.ms_pedidos_smartlogix.exception.BusinessException;
import com.example.ms_pedidos_smartlogix.integration.dto.ReservaInventarioDetalleRequestDTO;
import com.example.ms_pedidos_smartlogix.integration.dto.ReservaInventarioRequestDTO;
import com.example.ms_pedidos_smartlogix.integration.service.InventarioIntegrationService;
import com.example.ms_pedidos_smartlogix.model.Cliente;
import com.example.ms_pedidos_smartlogix.model.DetallePedido;
import com.example.ms_pedidos_smartlogix.model.EstadoPedidoHistorial;
import com.example.ms_pedidos_smartlogix.model.EventoOutboxPedido;
import com.example.ms_pedidos_smartlogix.model.Pedido;
import com.example.ms_pedidos_smartlogix.repository.ClienteRepository;
import com.example.ms_pedidos_smartlogix.repository.DetallePedidoRepository;
import com.example.ms_pedidos_smartlogix.repository.EstadoPedidoHistorialRepository;
import com.example.ms_pedidos_smartlogix.repository.EventoOutboxPedidoRepository;
import com.example.ms_pedidos_smartlogix.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final EstadoPedidoHistorialRepository estadoHistorialRepository;
    private final EventoOutboxPedidoRepository eventoOutboxRepository;
    private final InventarioIntegrationService inventarioIntegrationService;

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository,
                         DetallePedidoRepository detallePedidoRepository,
                         EstadoPedidoHistorialRepository estadoHistorialRepository,
                         EventoOutboxPedidoRepository eventoOutboxRepository,
                         InventarioIntegrationService inventarioIntegrationService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.estadoHistorialRepository = estadoHistorialRepository;
        this.eventoOutboxRepository = eventoOutboxRepository;
        this.inventarioIntegrationService = inventarioIntegrationService;
    }

    @Transactional
    public PedidoResponseDTO crearPedido(PedidoRequestDTO dto) {
        // Validar que el cliente existe
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new BusinessException("Cliente con ID " + dto.getIdCliente() + " no existe"));

        // Validar que el código de pedido no existe
        if (pedidoRepository.existsByCodigoPedido(dto.getCodigoPedido())) {
            throw new BusinessException("El código de pedido " + dto.getCodigoPedido() + " ya existe");
        }

        // Crear el pedido
        Pedido pedido = new Pedido(
            cliente,
            dto.getCodigoPedido(),
            dto.getCanalOrigen(),
            dto.getTotalBruto(),
            dto.getDescuentoTotal(),
            dto.getTotalNeto(),
            dto.getObservacion()
        );

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // Crear registro en estado_pedido_historial con estado CREADO
        EstadoPedidoHistorial estadoHistorial = new EstadoPedidoHistorial(
            pedidoGuardado,
            "CREADO",
            "Pedido creado",
            "SISTEMA"
        );
        estadoHistorialRepository.save(estadoHistorial);

        // Crear evento en evento_outbox_pedido con tipo_evento = "PEDIDO_CREADO"
        EventoOutboxPedido evento = new EventoOutboxPedido(
            pedidoGuardado,
            "PEDIDO_CREADO",
            "{\"idPedido\": " + pedidoGuardado.getIdPedido() + ", \"codigoPedido\": \"" + pedidoGuardado.getCodigoPedido() + "\"}",
            "PENDIENTE"
        );
        eventoOutboxRepository.save(evento);

        return convertToResponseDTO(pedidoGuardado, cliente);
    }

    public List<PedidoResponseDTO> listarPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidos.stream()
                .map(pedido -> convertToResponseDTO(pedido, pedido.getCliente()))
                .collect(Collectors.toList());
    }

    public PedidoResponseDTO obtenerPedidoPorId(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new BusinessException("Pedido con ID " + idPedido + " no existe"));
        return convertToResponseDTO(pedido, pedido.getCliente());
    }

    public List<PedidoResponseDTO> listarPedidosPorCliente(Long idCliente) {
        if (!clienteRepository.existsById(idCliente)) {
            throw new BusinessException("Cliente con ID " + idCliente + " no existe");
        }
        List<Pedido> pedidos = pedidoRepository.findByCliente_IdCliente(idCliente);
        return pedidos.stream()
                .map(pedido -> convertToResponseDTO(pedido, pedido.getCliente()))
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoResponseDTO cambiarEstadoPedido(Long idPedido, String nuevoEstado, String usuarioResponsable, String observacion) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new BusinessException("Pedido con ID " + idPedido + " no existe"));

        // Si el nuevo estado es CONFIRMADO, reservar inventario antes de confirmar
        if ("CONFIRMADO".equalsIgnoreCase(nuevoEstado)) {
            reservarInventarioParaPedido(pedido);
        }

        // Actualizar estado_actual en pedido
        pedido.setEstadoActual(nuevoEstado);
        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        // Crear registro en estado_pedido_historial
        EstadoPedidoHistorial estadoHistorial = new EstadoPedidoHistorial(
            pedidoActualizado,
            nuevoEstado,
            observacion,
            usuarioResponsable
        );
        estadoHistorialRepository.save(estadoHistorial);

        // Crear evento outbox con tipo_evento = "PEDIDO_ESTADO_CAMBIADO"
        EventoOutboxPedido evento = new EventoOutboxPedido(
            pedidoActualizado,
            "PEDIDO_ESTADO_CAMBIADO",
            "{\"idPedido\": " + pedidoActualizado.getIdPedido() + ", \"nuevoEstado\": \"" + nuevoEstado + "\"}",
            "PENDIENTE"
        );
        eventoOutboxRepository.save(evento);

        return convertToResponseDTO(pedidoActualizado, pedidoActualizado.getCliente());
    }

    public void eliminarPedido(Long idPedido) {
        if (!pedidoRepository.existsById(idPedido)) {
            throw new BusinessException("Pedido con ID " + idPedido + " no existe");
        }
        pedidoRepository.deleteById(idPedido);
    }

    private void reservarInventarioParaPedido(Pedido pedido) {
        List<DetallePedido> detalles = detallePedidoRepository.findByPedido_IdPedido(pedido.getIdPedido());

        if (detalles == null || detalles.isEmpty()) {
            throw new BusinessException("No se puede confirmar el pedido porque no tiene detalles asociados");
        }

        ReservaInventarioRequestDTO requestDTO = new ReservaInventarioRequestDTO();
        requestDTO.setIdPedidoRef(pedido.getIdPedido());
        requestDTO.setCodigoPedidoRef(pedido.getCodigoPedido());

        List<ReservaInventarioDetalleRequestDTO> detallesReserva = detalles.stream()
                .map(detalle -> {
                    ReservaInventarioDetalleRequestDTO detalleDTO = new ReservaInventarioDetalleRequestDTO();
                    detalleDTO.setIdProducto(detalle.getIdProductoRef());
                    // MVP: Usar bodega 1 por defecto mientras no exista lógica de selección de bodega
                    detalleDTO.setIdBodega(1L);
                    detalleDTO.setCantidadReservada(detalle.getCantidad());
                    return detalleDTO;
                })
                .collect(Collectors.toList());

        requestDTO.setDetalles(detallesReserva);

        inventarioIntegrationService.reservarInventario(requestDTO);
    }

    private PedidoResponseDTO convertToResponseDTO(Pedido pedido, Cliente cliente) {
        return new PedidoResponseDTO(
            pedido.getIdPedido(),
            pedido.getCliente().getIdCliente(),
            cliente.getNombre() + " " + cliente.getApellido(),
            pedido.getCodigoPedido(),
            pedido.getFechaCreacion(),
            pedido.getEstadoActual(),
            pedido.getCanalOrigen(),
            pedido.getTotalBruto(),
            pedido.getDescuentoTotal(),
            pedido.getTotalNeto(),
            pedido.getObservacion()
        );
    }
}
