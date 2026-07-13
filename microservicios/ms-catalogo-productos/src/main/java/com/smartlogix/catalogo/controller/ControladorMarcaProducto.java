package com.smartlogix.catalogo.controller;

import com.smartlogix.catalogo.dto.response.RespuestaMarcaProducto;
import com.smartlogix.catalogo.dto.request.SolicitudMarcaProducto;
import com.smartlogix.catalogo.service.ServicioMarcaProducto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operaciones CRUD sobre marcas de productos.
 */
@RestController
@RequestMapping("/api/catalogo/marcas")
@RequiredArgsConstructor
public class ControladorMarcaProducto {

    private final ServicioMarcaProducto servicioMarcaProducto;

    @GetMapping
    public ResponseEntity<List<RespuestaMarcaProducto>> listar() {
        return ResponseEntity.ok(servicioMarcaProducto.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaMarcaProducto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(servicioMarcaProducto.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<RespuestaMarcaProducto> crear(@Valid @RequestBody SolicitudMarcaProducto solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioMarcaProducto.crear(solicitud));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaMarcaProducto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudMarcaProducto solicitud) {
        return ResponseEntity.ok(servicioMarcaProducto.actualizar(id, solicitud));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<RespuestaMarcaProducto> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(servicioMarcaProducto.desactivar(id));
    }
}
