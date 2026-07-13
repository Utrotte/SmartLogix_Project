package com.smartlogix.pedidos.controller;

import com.smartlogix.pedidos.dto.*;
import com.smartlogix.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listar() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtener(@PathVariable Long id) {
        PedidoResponse r = pedidoService.obtener(id);
        if (r == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(r);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<PedidoResponse>> porCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(pedidoService.porCliente(idCliente));
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody CrearPedidoRequest req) {
        PedidoResponse r = pedidoService.crear(req);
        return ResponseEntity.ok(r);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponse> actualizarEstado(@PathVariable Long id, @Valid @RequestBody ActualizarEstadoPedidoRequest req) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, req));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelar(id));
    }

}
