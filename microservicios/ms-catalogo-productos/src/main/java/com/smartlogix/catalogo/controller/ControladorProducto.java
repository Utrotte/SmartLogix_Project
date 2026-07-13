package com.smartlogix.catalogo.controller;

import com.smartlogix.catalogo.dto.response.RespuestaImagenProducto;
import com.smartlogix.catalogo.dto.response.RespuestaPrecioProducto;
import com.smartlogix.catalogo.dto.response.RespuestaProducto;
import com.smartlogix.catalogo.dto.request.SolicitudImagenProducto;
import com.smartlogix.catalogo.dto.request.SolicitudPrecioProducto;
import com.smartlogix.catalogo.dto.request.SolicitudProducto;
import com.smartlogix.catalogo.service.ServicioImagenProducto;
import com.smartlogix.catalogo.service.ServicioPrecioProducto;
import com.smartlogix.catalogo.service.ServicioProducto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operaciones CRUD sobre productos y sus recursos anidados.
 */
@RestController
@RequestMapping("/api/catalogo/productos")
@RequiredArgsConstructor
public class ControladorProducto {

    private final ServicioProducto servicioProducto;
    private final ServicioPrecioProducto servicioPrecioProducto;
    private final ServicioImagenProducto servicioImagenProducto;

    @GetMapping
    public ResponseEntity<List<RespuestaProducto>> listar() {
        return ResponseEntity.ok(servicioProducto.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaProducto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(servicioProducto.obtenerPorId(id));
    }

    @GetMapping("/sku/{codigoSku}")
    public ResponseEntity<RespuestaProducto> obtenerPorSku(@PathVariable String codigoSku) {
        return ResponseEntity.ok(servicioProducto.obtenerPorCodigoSku(codigoSku));
    }

    @PostMapping
    public ResponseEntity<RespuestaProducto> crear(@Valid @RequestBody SolicitudProducto solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioProducto.crear(solicitud));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaProducto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudProducto solicitud) {
        return ResponseEntity.ok(servicioProducto.actualizar(id, solicitud));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<RespuestaProducto> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(servicioProducto.desactivar(id));
    }

    @GetMapping("/{id}/precios")
    public ResponseEntity<List<RespuestaPrecioProducto>> listarPrecios(@PathVariable Long id) {
        return ResponseEntity.ok(servicioPrecioProducto.listarPorProducto(id));
    }

    @PostMapping("/{id}/precios")
    public ResponseEntity<RespuestaPrecioProducto> crearPrecio(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudPrecioProducto solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioPrecioProducto.crear(id, solicitud));
    }

    @GetMapping("/{id}/imagenes")
    public ResponseEntity<List<RespuestaImagenProducto>> listarImagenes(@PathVariable Long id) {
        return ResponseEntity.ok(servicioImagenProducto.listarPorProducto(id));
    }

    @PostMapping("/{id}/imagenes")
    public ResponseEntity<RespuestaImagenProducto> crearImagen(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudImagenProducto solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioImagenProducto.crear(id, solicitud));
    }
}
