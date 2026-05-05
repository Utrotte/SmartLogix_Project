package com.example.ms_pedidos_smartlogix.controller;

import com.example.ms_pedidos_smartlogix.dto.PagoPedidoRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.PagoPedidoResponseDTO;
import com.example.ms_pedidos_smartlogix.service.PagoPedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pagos-pedido")
public class PagoPedidoController {

    private final PagoPedidoService pagoPedidoService;

    public PagoPedidoController(PagoPedidoService pagoPedidoService) {
        this.pagoPedidoService = pagoPedidoService;
    }

    @PostMapping
    public ResponseEntity<PagoPedidoResponseDTO> registrarPago(@RequestBody PagoPedidoRequestDTO dto) {
        PagoPedidoResponseDTO response = pagoPedidoService.registrarPago(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PagoPedidoResponseDTO>> listarPagos() {
        List<PagoPedidoResponseDTO> pagos = pagoPedidoService.listarPagos();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{idPago}")
    public ResponseEntity<PagoPedidoResponseDTO> obtenerPagoPorId(@PathVariable Long idPago) {
        PagoPedidoResponseDTO pago = pagoPedidoService.obtenerPagoPorId(idPago);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<List<PagoPedidoResponseDTO>> listarPagosPorPedido(@PathVariable Long idPedido) {
        List<PagoPedidoResponseDTO> pagos = pagoPedidoService.listarPagosPorPedido(idPedido);
        return ResponseEntity.ok(pagos);
    }
}
