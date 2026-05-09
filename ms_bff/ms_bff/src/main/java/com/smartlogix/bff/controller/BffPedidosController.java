package com.smartlogix.bff.controller;

import com.smartlogix.bff.service.BffPedidosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bff/pedidos")
public class BffPedidosController {

    private final BffPedidosService pedidosService;

    public BffPedidosController(BffPedidosService pedidosService) {
        this.pedidosService = pedidosService;
    }

    @GetMapping("/")
    public ResponseEntity<List<?>> listarPedidos() {
        List<?> response = pedidosService.listarPedidos();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/")
    public ResponseEntity<Object> crearPedido(@RequestBody Map<String, Object> request) {
        Object response = pedidosService.crearPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{idPedido}")
    public ResponseEntity<Object> buscarPedido(@PathVariable("idPedido") Long idPedido) {
        Object response = pedidosService.buscarPedido(idPedido);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idPedido}")
    public ResponseEntity<Object> actualizarPedido(
            @PathVariable("idPedido") Long idPedido,
            @RequestBody Map<String, Object> request) {
        Object response = pedidosService.actualizarPedido(idPedido, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{idPedido}/cancelar")
    public ResponseEntity<Object> cancelarPedido(@PathVariable("idPedido") Long idPedido) {
        Object response = pedidosService.cancelarPedido(idPedido);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{idPedido}/estado")
    public ResponseEntity<Object> cambiarEstadoPedido(
            @PathVariable("idPedido") Long idPedido,
            @RequestBody Map<String, Object> request) {
        Object response = pedidosService.cambiarEstadoPedido(idPedido, request);
        return ResponseEntity.ok(response);
    }
}
