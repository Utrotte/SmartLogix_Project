package com.smartlogix.catalogo.controller;

import com.smartlogix.catalogo.dto.response.RespuestaCategoriaProducto;
import com.smartlogix.catalogo.dto.request.SolicitudCategoriaProducto;
import com.smartlogix.catalogo.service.ServicioCategoriaProducto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operaciones CRUD sobre categorías de productos.
 */
@RestController
@RequestMapping("/api/catalogo/categorias")
@RequiredArgsConstructor
public class ControladorCategoriaProducto {

    private final ServicioCategoriaProducto servicioCategoriaProducto;

    @GetMapping
    public ResponseEntity<List<RespuestaCategoriaProducto>> listar() {
        return ResponseEntity.ok(servicioCategoriaProducto.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaCategoriaProducto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(servicioCategoriaProducto.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<RespuestaCategoriaProducto> crear(@Valid @RequestBody SolicitudCategoriaProducto solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioCategoriaProducto.crear(solicitud));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaCategoriaProducto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudCategoriaProducto solicitud) {
        return ResponseEntity.ok(servicioCategoriaProducto.actualizar(id, solicitud));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<RespuestaCategoriaProducto> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(servicioCategoriaProducto.desactivar(id));
    }
}
