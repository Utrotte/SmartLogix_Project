package com.smartlogix.inventario_service.controller;

import com.smartlogix.inventario_service.dto.request.ProductoRequest;
import com.smartlogix.inventario_service.dto.response.ProductoResponse;
import com.smartlogix.inventario_service.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // POST: crear nuevo producto
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(
            @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoService.crear(request));
    }

    // GET: listar todos los productos
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    // GET: obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    // GET: obtener producto por SKU
    @GetMapping("/sku/{codigoSku}")
    public ResponseEntity<ProductoResponse> obtenerPorSku(@PathVariable String codigoSku) {
        return ResponseEntity.ok(productoService.obtenerPorSku(codigoSku));
    }

    // PUT: actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    // PATCH: desactivar producto (baja lógica)
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ProductoResponse> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.desactivar(id));
    }
}
