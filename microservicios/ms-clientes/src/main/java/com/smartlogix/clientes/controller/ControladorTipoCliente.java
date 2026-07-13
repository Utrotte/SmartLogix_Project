package com.smartlogix.clientes.controller;

import com.smartlogix.clientes.dto.response.RespuestaTipoCliente;
import com.smartlogix.clientes.dto.request.SolicitudTipoCliente;
import com.smartlogix.clientes.service.ServicioTipoCliente;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operaciones sobre tipos de cliente.
 */
@RestController
@RequestMapping("/api/clientes/tipos")
@RequiredArgsConstructor
public class ControladorTipoCliente {

    private final ServicioTipoCliente servicioTipoCliente;

    @GetMapping
    public ResponseEntity<List<RespuestaTipoCliente>> listar() {
        return ResponseEntity.ok(servicioTipoCliente.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaTipoCliente> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(servicioTipoCliente.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<RespuestaTipoCliente> crear(@Valid @RequestBody SolicitudTipoCliente solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioTipoCliente.crear(solicitud));
    }
}
