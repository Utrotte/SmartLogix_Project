package com.smartlogix.inventario_service.controller;

import com.smartlogix.inventario_service.dto.request.ConfirmarReservaRequest;
import com.smartlogix.inventario_service.dto.request.LiberarReservaRequest;
import com.smartlogix.inventario_service.dto.request.ReservarStockRequest;
import com.smartlogix.inventario_service.dto.response.ReservaInventarioResponse;
import com.smartlogix.inventario_service.service.ReservaInventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario/reservas")
@RequiredArgsConstructor
public class ReservaInventarioController {

    private final ReservaInventarioService reservaInventarioService;

    // POST: crear reserva de stock
    @PostMapping
    public ResponseEntity<ReservaInventarioResponse> reservarStock(
            @Valid @RequestBody ReservarStockRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservaInventarioService.reservarStock(request));
    }

    // GET: listar todas las reservas
    @GetMapping
    public ResponseEntity<List<ReservaInventarioResponse>> listarTodas() {
        return ResponseEntity.ok(reservaInventarioService.listarTodas());
    }

    // GET: obtener reserva por ID
    @GetMapping("/{idReserva}")
    public ResponseEntity<ReservaInventarioResponse> obtenerPorId(
            @PathVariable Long idReserva) {
        return ResponseEntity.ok(reservaInventarioService.obtenerPorId(idReserva));
    }

    // GET: listar reservas asociadas a un pedido
    @GetMapping("/pedido/{idPedidoRef}")
    public ResponseEntity<List<ReservaInventarioResponse>> obtenerPorPedido(
            @PathVariable Long idPedidoRef) {
        return ResponseEntity.ok(reservaInventarioService.obtenerPorPedido(idPedidoRef));
    }

    // POST: confirmar reserva (stock comprometido)
    @PostMapping("/{idReserva}/confirmar")
    public ResponseEntity<ReservaInventarioResponse> confirmarReserva(
            @PathVariable Long idReserva,
            @RequestBody(required = false) ConfirmarReservaRequest request) {
        String observacion = request != null ? request.getObservacion() : null;
        return ResponseEntity.ok(reservaInventarioService.confirmarReserva(idReserva, observacion));
    }

    // POST: liberar/cancelar reserva
    @PostMapping("/{idReserva}/liberar")
    public ResponseEntity<ReservaInventarioResponse> liberarReserva(
            @PathVariable Long idReserva,
            @RequestBody(required = false) LiberarReservaRequest request) {
        String observacion = request != null ? request.getObservacion() : null;
        return ResponseEntity.ok(reservaInventarioService.liberarReserva(idReserva, observacion));
    }
}
