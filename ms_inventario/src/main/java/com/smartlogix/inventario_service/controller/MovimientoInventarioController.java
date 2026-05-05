package com.smartlogix.inventario_service.controller;

import com.smartlogix.inventario_service.dto.response.MovimientoInventarioResponse;
import com.smartlogix.inventario_service.service.MovimientoInventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario/movimientos")
@RequiredArgsConstructor
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoInventarioService;

    // GET: listar todos los movimientos de inventario
    @GetMapping
    public ResponseEntity<List<MovimientoInventarioResponse>> listarTodos() {
        return ResponseEntity.ok(movimientoInventarioService.listarTodos());
    }

    // GET: obtener movimiento por ID
    @GetMapping("/{idMovimiento}")
    public ResponseEntity<MovimientoInventarioResponse> obtenerPorId(
            @PathVariable Long idMovimiento) {
        return ResponseEntity.ok(movimientoInventarioService.obtenerPorId(idMovimiento));
    }

    // GET: listar movimientos de un producto
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<MovimientoInventarioResponse>> obtenerPorProducto(
            @PathVariable Long idProducto) {
        return ResponseEntity.ok(movimientoInventarioService.obtenerPorProducto(idProducto));
    }

    // GET: listar movimientos en una bodega
    @GetMapping("/bodega/{idBodega}")
    public ResponseEntity<List<MovimientoInventarioResponse>> obtenerPorBodega(
            @PathVariable Long idBodega) {
        return ResponseEntity.ok(movimientoInventarioService.obtenerPorBodega(idBodega));
    }
}
