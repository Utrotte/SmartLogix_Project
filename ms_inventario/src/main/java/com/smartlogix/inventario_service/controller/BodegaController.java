package com.smartlogix.inventario_service.controller;

import com.smartlogix.inventario_service.dto.request.BodegaRequest;
import com.smartlogix.inventario_service.dto.response.BodegaResponse;
import com.smartlogix.inventario_service.service.BodegaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario/bodegas")
@RequiredArgsConstructor
public class BodegaController {

    private final BodegaService bodegaService;

    // POST: crear nueva bodega
    @PostMapping
    public ResponseEntity<BodegaResponse> crear(
            @Valid @RequestBody BodegaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bodegaService.crear(request));
    }

    // GET: listar todas las bodegas
    @GetMapping
    public ResponseEntity<List<BodegaResponse>> listarTodas() {
        return ResponseEntity.ok(bodegaService.listarTodas());
    }

    // GET: obtener bodega por ID
    @GetMapping("/{id}")
    public ResponseEntity<BodegaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.obtenerPorId(id));
    }

    // PUT: actualizar bodega
    @PutMapping("/{id}")
    public ResponseEntity<BodegaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody BodegaRequest request) {
        return ResponseEntity.ok(bodegaService.actualizar(id, request));
    }

    // PATCH: desactivar bodega (baja lógica)
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<BodegaResponse> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.desactivar(id));
    }
}
