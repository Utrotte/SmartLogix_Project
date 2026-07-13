package com.smartlogix.clientes.controller;

import com.smartlogix.clientes.dto.response.RespuestaContactoCliente;
import com.smartlogix.clientes.dto.request.SolicitudContactoCliente;
import com.smartlogix.clientes.service.ServicioContactoCliente;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para operaciones sobre contactos a nivel de recurso independiente.
 */
@RestController
@RequestMapping("/api/clientes/contactos")
@RequiredArgsConstructor
public class ControladorContactoCliente {

    private final ServicioContactoCliente servicioContactoCliente;

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaContactoCliente> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudContactoCliente solicitud) {
        return ResponseEntity.ok(servicioContactoCliente.actualizar(id, solicitud));
    }
}
