package com.example.ms_pedidos_smartlogix.controller;

import com.example.ms_pedidos_smartlogix.dto.EstadoPedidoHistorialRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.EstadoPedidoHistorialResponseDTO;
import com.example.ms_pedidos_smartlogix.service.EstadoPedidoHistorialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historial-pedidos")
public class EstadoPedidoHistorialController {

    private final EstadoPedidoHistorialService estadoPedidoHistorialService;

    public EstadoPedidoHistorialController(EstadoPedidoHistorialService estadoPedidoHistorialService) {
        this.estadoPedidoHistorialService = estadoPedidoHistorialService;
    }

    @PostMapping
    public ResponseEntity<EstadoPedidoHistorialResponseDTO> registrarEstado(@RequestBody EstadoPedidoHistorialRequestDTO dto) {
        EstadoPedidoHistorialResponseDTO response = estadoPedidoHistorialService.registrarEstado(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EstadoPedidoHistorialResponseDTO>> listarHistorial() {
        List<EstadoPedidoHistorialResponseDTO> historial = estadoPedidoHistorialService.listarHistorial();
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<List<EstadoPedidoHistorialResponseDTO>> listarHistorialPorPedido(@PathVariable Long idPedido) {
        List<EstadoPedidoHistorialResponseDTO> historial = estadoPedidoHistorialService.listarHistorialPorPedido(idPedido);
        return ResponseEntity.ok(historial);
    }
}
