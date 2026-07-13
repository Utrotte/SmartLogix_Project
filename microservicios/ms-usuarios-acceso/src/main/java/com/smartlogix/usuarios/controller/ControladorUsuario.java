package com.smartlogix.usuarios.controller;

import com.smartlogix.usuarios.dto.request.SolicitudUsuario;
import com.smartlogix.usuarios.dto.response.RespuestaUsuario;
import com.smartlogix.usuarios.service.ServicioUsuario;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class ControladorUsuario {

    private final ServicioUsuario servicioUsuario;

    public ControladorUsuario(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @PostMapping
    public ResponseEntity<RespuestaUsuario> crear(@Valid @RequestBody SolicitudUsuario solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioUsuario.crear(solicitud));
    }

    @GetMapping
    public ResponseEntity<List<RespuestaUsuario>> listar() {
        return ResponseEntity.ok(servicioUsuario.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaUsuario> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(servicioUsuario.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaUsuario> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody SolicitudUsuario solicitud) {
        return ResponseEntity.ok(servicioUsuario.actualizar(id, solicitud));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<RespuestaUsuario> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(servicioUsuario.desactivar(id));
    }
}
