package com.smartlogix.clientes.controller;

import com.smartlogix.clientes.dto.response.RespuestaCliente;
import com.smartlogix.clientes.dto.response.RespuestaContactoCliente;
import com.smartlogix.clientes.dto.response.RespuestaDireccionCliente;
import com.smartlogix.clientes.dto.request.SolicitudCliente;
import com.smartlogix.clientes.dto.request.SolicitudContactoCliente;
import com.smartlogix.clientes.dto.request.SolicitudDireccionCliente;
import com.smartlogix.clientes.service.ServicioCliente;
import com.smartlogix.clientes.service.ServicioContactoCliente;
import com.smartlogix.clientes.service.ServicioDireccionCliente;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operaciones CRUD sobre clientes y sus recursos anidados.
 */
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ControladorCliente {

    private final ServicioCliente servicioCliente;
    private final ServicioDireccionCliente servicioDireccionCliente;
    private final ServicioContactoCliente servicioContactoCliente;

    @GetMapping
    public ResponseEntity<List<RespuestaCliente>> listar() {
        return ResponseEntity.ok(servicioCliente.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaCliente> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(servicioCliente.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<RespuestaCliente> crear(@Valid @RequestBody SolicitudCliente solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioCliente.crear(solicitud));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaCliente> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudCliente solicitud) {
        return ResponseEntity.ok(servicioCliente.actualizar(id, solicitud));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<RespuestaCliente> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(servicioCliente.desactivar(id));
    }

    @GetMapping("/{idCliente}/direcciones")
    public ResponseEntity<List<RespuestaDireccionCliente>> listarDirecciones(@PathVariable Long idCliente) {
        return ResponseEntity.ok(servicioDireccionCliente.listarPorCliente(idCliente));
    }

    @PostMapping("/{idCliente}/direcciones")
    public ResponseEntity<RespuestaDireccionCliente> crearDireccion(
            @PathVariable Long idCliente,
            @Valid @RequestBody SolicitudDireccionCliente solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(servicioDireccionCliente.crear(idCliente, solicitud));
    }

    @GetMapping("/{idCliente}/contactos")
    public ResponseEntity<List<RespuestaContactoCliente>> listarContactos(@PathVariable Long idCliente) {
        return ResponseEntity.ok(servicioContactoCliente.listarPorCliente(idCliente));
    }

    @PostMapping("/{idCliente}/contactos")
    public ResponseEntity<RespuestaContactoCliente> crearContacto(
            @PathVariable Long idCliente,
            @Valid @RequestBody SolicitudContactoCliente solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(servicioContactoCliente.crear(idCliente, solicitud));
    }
}
