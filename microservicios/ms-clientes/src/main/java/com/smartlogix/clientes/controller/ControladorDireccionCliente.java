package com.smartlogix.clientes.controller;

import com.smartlogix.clientes.dto.response.RespuestaDireccionCliente;
import com.smartlogix.clientes.dto.request.SolicitudDireccionCliente;
import com.smartlogix.clientes.service.ServicioDireccionCliente;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para operaciones sobre direcciones a nivel de recurso independiente.
 */
@RestController
@RequestMapping("/api/clientes/direcciones")
@RequiredArgsConstructor
public class ControladorDireccionCliente {

    private final ServicioDireccionCliente servicioDireccionCliente;

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaDireccionCliente> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudDireccionCliente solicitud) {
        return ResponseEntity.ok(servicioDireccionCliente.actualizar(id, solicitud));
    }
}
