package com.smartlogix.inventario_service.controller;

import com.smartlogix.inventario_service.dto.request.AjustarStockRequest;
import com.smartlogix.inventario_service.dto.request.ExistenciaRequest;
import com.smartlogix.inventario_service.dto.response.ExistenciaResponse;
import com.smartlogix.inventario_service.service.ExistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario/existencias")
@RequiredArgsConstructor
public class ExistenciaController {

    private final ExistenciaService existenciaService;

    // POST: crear nueva existencia
    @PostMapping
    public ResponseEntity<ExistenciaResponse> crear(
            @Valid @RequestBody ExistenciaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(existenciaService.crear(request));
    }

    // GET: listar existencias por producto
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<ExistenciaResponse>> obtenerPorProducto(
            @PathVariable Long idProducto) {
        return ResponseEntity.ok(existenciaService.obtenerPorProducto(idProducto));
    }

    // GET: listar existencias por bodega
    @GetMapping("/bodega/{idBodega}")
    public ResponseEntity<List<ExistenciaResponse>> obtenerPorBodega(
            @PathVariable Long idBodega) {
        return ResponseEntity.ok(existenciaService.obtenerPorBodega(idBodega));
    }

    // GET: obtener existencia de un producto en una bodega específ ica
    @GetMapping("/producto/{idProducto}/bodega/{idBodega}")
    public ResponseEntity<ExistenciaResponse> obtenerPorProductoYBodega(
            @PathVariable Long idProducto,
            @PathVariable Long idBodega) {
        return ResponseEntity.ok(existenciaService.obtenerPorProductoYBodega(idProducto, idBodega));
    }

    // PUT: ajustar cantidad de stock
    @PutMapping("/{idExistencia}/ajustar-stock")
    public ResponseEntity<ExistenciaResponse> ajustarStock(
            @PathVariable Long idExistencia,
            @Valid @RequestBody AjustarStockRequest request) {
        return ResponseEntity.ok(existenciaService.ajustarStock(
                idExistencia,
                request.getAjuste(),
                request.getObservacion()));
    }
}
