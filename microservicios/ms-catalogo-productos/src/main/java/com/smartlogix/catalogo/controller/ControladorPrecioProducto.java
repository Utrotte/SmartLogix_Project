package com.smartlogix.catalogo.controller;

import com.smartlogix.catalogo.dto.response.RespuestaPrecioProducto;
import com.smartlogix.catalogo.service.ServicioPrecioProducto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para operaciones sobre precios a nivel de recurso independiente.
 */
@RestController
@RequestMapping("/api/catalogo/precios")
@RequiredArgsConstructor
public class ControladorPrecioProducto {

    private final ServicioPrecioProducto servicioPrecioProducto;

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<RespuestaPrecioProducto> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(servicioPrecioProducto.desactivar(id));
    }
}
