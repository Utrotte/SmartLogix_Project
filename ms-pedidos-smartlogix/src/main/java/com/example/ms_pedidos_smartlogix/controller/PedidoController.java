package com.example.ms_pedidos_smartlogix.controller;

import com.example.ms_pedidos_smartlogix.dto.PedidoRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.PedidoResponseDTO;
import com.example.ms_pedidos_smartlogix.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crearPedido(@RequestBody PedidoRequestDTO dto) {
        PedidoResponseDTO response = pedidoService.crearPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos() {
        List<PedidoResponseDTO> pedidos = pedidoService.listarPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{idPedido}")
    public ResponseEntity<PedidoResponseDTO> obtenerPedidoPorId(@PathVariable Long idPedido) {
        PedidoResponseDTO pedido = pedidoService.obtenerPedidoPorId(idPedido);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidosPorCliente(@PathVariable Long idCliente) {
        List<PedidoResponseDTO> pedidos = pedidoService.listarPedidosPorCliente(idCliente);
        return ResponseEntity.ok(pedidos);
    }

    @PatchMapping("/{idPedido}/estado")
    public ResponseEntity<PedidoResponseDTO> cambiarEstadoPedido(
            @PathVariable Long idPedido,
            @RequestParam String nuevoEstado,
            @RequestParam String usuarioResponsable,
            @RequestParam String observacion) {
        PedidoResponseDTO response = pedidoService.cambiarEstadoPedido(idPedido, nuevoEstado, usuarioResponsable, observacion);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{idPedido}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long idPedido) {
        pedidoService.eliminarPedido(idPedido);
        return ResponseEntity.noContent().build();
    }
}
