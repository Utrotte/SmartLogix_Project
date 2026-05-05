package com.example.ms_pedidos_smartlogix.service;

import com.example.ms_pedidos_smartlogix.dto.PedidoRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.PedidoResponseDTO;
import com.example.ms_pedidos_smartlogix.model.Cliente;
import com.example.ms_pedidos_smartlogix.model.EstadoPedidoHistorial;
import com.example.ms_pedidos_smartlogix.model.EventoOutboxPedido;
import com.example.ms_pedidos_smartlogix.model.Pedido;
import com.example.ms_pedidos_smartlogix.repository.ClienteRepository;
import com.example.ms_pedidos_smartlogix.repository.EstadoPedidoHistorialRepository;
import com.example.ms_pedidos_smartlogix.repository.EventoOutboxPedidoRepository;
import com.example.ms_pedidos_smartlogix.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final EstadoPedidoHistorialRepository estadoHistorialRepository;
    private final EventoOutboxPedidoRepository eventoOutboxRepository;

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository,
                         EstadoPedidoHistorialRepository estadoHistorialRepository,
                         EventoOutboxPedidoRepository eventoOutboxRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.estadoHistorialRepository = estadoHistorialRepository;
        this.eventoOutboxRepository = eventoOutboxRepository;
    }

    public PedidoResponseDTO crearPedido(PedidoRequestDTO dto) {
        // Validar que el cliente existe
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + dto.getIdCliente() + " no existe"));

        // Validar que el código de pedido no existe
        if (pedidoRepository.existsByCodigoPedido(dto.getCodigoPedido())) {
            throw new RuntimeException("El código de pedido " + dto.getCodigoPedido() + " ya existe");
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
                .orElseThrow(() -> new RuntimeException("Pedido con ID " + idPedido + " no existe"));
        return convertToResponseDTO(pedido, pedido.getCliente());
    }

    public List<PedidoResponseDTO> listarPedidosPorCliente(Long idCliente) {
        if (!clienteRepository.existsById(idCliente)) {
            throw new RuntimeException("Cliente con ID " + idCliente + " no existe");
        }
        List<Pedido> pedidos = pedidoRepository.findByCliente_IdCliente(idCliente);
        return pedidos.stream()
                .map(pedido -> convertToResponseDTO(pedido, pedido.getCliente()))
                .collect(Collectors.toList());
    }

    public PedidoResponseDTO cambiarEstadoPedido(Long idPedido, String nuevoEstado, String usuarioResponsable, String observacion) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido con ID " + idPedido + " no existe"));

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
            throw new RuntimeException("Pedido con ID " + idPedido + " no existe");
        }
        pedidoRepository.deleteById(idPedido);
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
