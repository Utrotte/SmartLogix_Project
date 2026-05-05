package com.smartlogix.inventario_service.controller;

import com.smartlogix.inventario_service.dto.request.CategoriaProductoRequest;
import com.smartlogix.inventario_service.dto.response.CategoriaProductoResponse;
import com.smartlogix.inventario_service.service.CategoriaProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario/categorias")
@RequiredArgsConstructor
public class CategoriaProductoController {

    private final CategoriaProductoService categoriaProductoService;

    // POST: crear nueva categoría
    @PostMapping
    public ResponseEntity<CategoriaProductoResponse> crear(
            @Valid @RequestBody CategoriaProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaProductoService.crear(request));
    }

    // GET: listar todas las categorías
    @GetMapping
    public ResponseEntity<List<CategoriaProductoResponse>> listarTodas() {
        return ResponseEntity.ok(categoriaProductoService.listarTodas());
    }

    // GET: obtener categoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProductoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaProductoService.obtenerPorId(id));
    }

    // PUT: actualizar categoría
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProductoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaProductoRequest request) {
        return ResponseEntity.ok(categoriaProductoService.actualizar(id, request));
    }

    // PATCH: desactivar categoría (baja lógica)
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<CategoriaProductoResponse> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaProductoService.desactivar(id));
    }
}
