package com.smartlogix.catalogo.controller;

import com.smartlogix.catalogo.service.ServicioImagenProducto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para operaciones sobre imágenes a nivel de recurso independiente.
 */
@RestController
@RequestMapping("/api/catalogo/imagenes")
@RequiredArgsConstructor
public class ControladorImagenProducto {

    private final ServicioImagenProducto servicioImagenProducto;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicioImagenProducto.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
