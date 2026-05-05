package com.example.ms_pedidos_smartlogix.controller;

import com.example.ms_pedidos_smartlogix.dto.EventoOutboxPedidoRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.EventoOutboxPedidoResponseDTO;
import com.example.ms_pedidos_smartlogix.service.EventoOutboxPedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/eventos-outbox-pedido")
public class EventoOutboxPedidoController {

    private final EventoOutboxPedidoService eventoOutboxPedidoService;

    public EventoOutboxPedidoController(EventoOutboxPedidoService eventoOutboxPedidoService) {
        this.eventoOutboxPedidoService = eventoOutboxPedidoService;
    }

    @PostMapping
    public ResponseEntity<EventoOutboxPedidoResponseDTO> crearEvento(@RequestBody EventoOutboxPedidoRequestDTO dto) {
        EventoOutboxPedidoResponseDTO response = eventoOutboxPedidoService.crearEvento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EventoOutboxPedidoResponseDTO>> listarEventos() {
        List<EventoOutboxPedidoResponseDTO> eventos = eventoOutboxPedidoService.listarEventos();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<EventoOutboxPedidoResponseDTO>> listarEventosPendientes() {
        List<EventoOutboxPedidoResponseDTO> eventos = eventoOutboxPedidoService.listarEventosPendientes();
        return ResponseEntity.ok(eventos);
    }

    @PatchMapping("/{idEvento}/publicar")
    public ResponseEntity<EventoOutboxPedidoResponseDTO> marcarComoPublicado(@PathVariable Long idEvento) {
        EventoOutboxPedidoResponseDTO response = eventoOutboxPedidoService.marcarComoPublicado(idEvento);
        return ResponseEntity.ok(response);
    }
}
