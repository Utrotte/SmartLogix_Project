package com.example.ms_pedidos_smartlogix.controller;

import com.example.ms_pedidos_smartlogix.dto.DetallePedidoRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.DetallePedidoResponseDTO;
import com.example.ms_pedidos_smartlogix.service.DetallePedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/detalles-pedido")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @PostMapping
    public ResponseEntity<DetallePedidoResponseDTO> agregarDetalle(@RequestBody DetallePedidoRequestDTO dto) {
        DetallePedidoResponseDTO response = detallePedidoService.agregarDetalle(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DetallePedidoResponseDTO>> listarDetalles() {
        List<DetallePedidoResponseDTO> detalles = detallePedidoService.listarDetalles();
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/{idDetalle}")
    public ResponseEntity<DetallePedidoResponseDTO> obtenerDetallePorId(@PathVariable Long idDetalle) {
        DetallePedidoResponseDTO detalle = detallePedidoService.obtenerDetallePorId(idDetalle);
        return ResponseEntity.ok(detalle);
    }

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<List<DetallePedidoResponseDTO>> listarDetallesPorPedido(@PathVariable Long idPedido) {
        List<DetallePedidoResponseDTO> detalles = detallePedidoService.listarDetallesPorPedido(idPedido);
        return ResponseEntity.ok(detalles);
    }

    @DeleteMapping("/{idDetalle}")
    public ResponseEntity<Void> eliminarDetalle(@PathVariable Long idDetalle) {
        detallePedidoService.eliminarDetalle(idDetalle);
        return ResponseEntity.noContent().build();
    }
}
