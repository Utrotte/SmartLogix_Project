package com.example.ms_pedidos_smartlogix.service;

import com.example.ms_pedidos_smartlogix.dto.EstadoPedidoHistorialRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.EstadoPedidoHistorialResponseDTO;
import com.example.ms_pedidos_smartlogix.model.EstadoPedidoHistorial;
import com.example.ms_pedidos_smartlogix.model.Pedido;
import com.example.ms_pedidos_smartlogix.repository.EstadoPedidoHistorialRepository;
import com.example.ms_pedidos_smartlogix.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoPedidoHistorialService {

    private final EstadoPedidoHistorialRepository estadoHistorialRepository;
    private final PedidoRepository pedidoRepository;

    public EstadoPedidoHistorialService(EstadoPedidoHistorialRepository estadoHistorialRepository,
                                        PedidoRepository pedidoRepository) {
        this.estadoHistorialRepository = estadoHistorialRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public EstadoPedidoHistorialResponseDTO registrarEstado(EstadoPedidoHistorialRequestDTO dto) {
        // Validar que el pedido existe
        Pedido pedido = pedidoRepository.findById(dto.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido con ID " + dto.getIdPedido() + " no existe"));

        // Crear el registro de estado
        EstadoPedidoHistorial estadoHistorial = new EstadoPedidoHistorial(
            pedido,
            dto.getEstado(),
            dto.getObservacion(),
            dto.getUsuarioResponsable()
        );

        EstadoPedidoHistorial estadoGuardado = estadoHistorialRepository.save(estadoHistorial);
        return convertToResponseDTO(estadoGuardado);
    }

    public List<EstadoPedidoHistorialResponseDTO> listarHistorial() {
        List<EstadoPedidoHistorial> historial = estadoHistorialRepository.findAll();
        return historial.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<EstadoPedidoHistorialResponseDTO> listarHistorialPorPedido(Long idPedido) {
        if (!pedidoRepository.existsById(idPedido)) {
            throw new RuntimeException("Pedido con ID " + idPedido + " no existe");
        }
        List<EstadoPedidoHistorial> historial = estadoHistorialRepository.findByPedido_IdPedido(idPedido);
        return historial.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private EstadoPedidoHistorialResponseDTO convertToResponseDTO(EstadoPedidoHistorial estadoHistorial) {
        return new EstadoPedidoHistorialResponseDTO(
            estadoHistorial.getIdEstadoHistorial(),
            estadoHistorial.getPedido().getIdPedido(),
            estadoHistorial.getEstado(),
            estadoHistorial.getFechaEstado(),
            estadoHistorial.getObservacion(),
            estadoHistorial.getUsuarioResponsable()
        );
    }
}
