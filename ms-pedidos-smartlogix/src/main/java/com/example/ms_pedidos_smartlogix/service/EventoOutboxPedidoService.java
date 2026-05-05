package com.example.ms_pedidos_smartlogix.service;

import com.example.ms_pedidos_smartlogix.dto.EventoOutboxPedidoRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.EventoOutboxPedidoResponseDTO;
import com.example.ms_pedidos_smartlogix.model.EventoOutboxPedido;
import com.example.ms_pedidos_smartlogix.model.Pedido;
import com.example.ms_pedidos_smartlogix.repository.EventoOutboxPedidoRepository;
import com.example.ms_pedidos_smartlogix.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoOutboxPedidoService {

    private final EventoOutboxPedidoRepository eventoOutboxRepository;
    private final PedidoRepository pedidoRepository;

    public EventoOutboxPedidoService(EventoOutboxPedidoRepository eventoOutboxRepository, PedidoRepository pedidoRepository) {
        this.eventoOutboxRepository = eventoOutboxRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public EventoOutboxPedidoResponseDTO crearEvento(EventoOutboxPedidoRequestDTO dto) {
        // Validar que el pedido existe
        Pedido pedido = pedidoRepository.findById(dto.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido con ID " + dto.getIdPedido() + " no existe"));

        // Crear el evento
        EventoOutboxPedido evento = new EventoOutboxPedido(
            pedido,
            dto.getTipoEvento(),
            dto.getPayload(),
            dto.getEstadoPublicacion()
        );

        EventoOutboxPedido eventoGuardado = eventoOutboxRepository.save(evento);
        return convertToResponseDTO(eventoGuardado);
    }

    public List<EventoOutboxPedidoResponseDTO> listarEventos() {
        List<EventoOutboxPedido> eventos = eventoOutboxRepository.findAll();
        return eventos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<EventoOutboxPedidoResponseDTO> listarEventosPendientes() {
        List<EventoOutboxPedido> eventos = eventoOutboxRepository.findByEstadoPublicacion("PENDIENTE");
        return eventos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public EventoOutboxPedidoResponseDTO marcarComoPublicado(Long idEvento) {
        EventoOutboxPedido evento = eventoOutboxRepository.findById(idEvento)
                .orElseThrow(() -> new RuntimeException("Evento con ID " + idEvento + " no existe"));

        evento.setEstadoPublicacion("PUBLICADO");
        evento.setFechaPublicacion(LocalDateTime.now());

        EventoOutboxPedido eventoActualizado = eventoOutboxRepository.save(evento);
        return convertToResponseDTO(eventoActualizado);
    }

    private EventoOutboxPedidoResponseDTO convertToResponseDTO(EventoOutboxPedido evento) {
        return new EventoOutboxPedidoResponseDTO(
            evento.getIdEvento(),
            evento.getPedido().getIdPedido(),
            evento.getTipoEvento(),
            evento.getPayload(),
            evento.getEstadoPublicacion(),
            evento.getFechaCreacion(),
            evento.getFechaPublicacion()
        );
    }
}
