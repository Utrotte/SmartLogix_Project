package com.smartlogix.usuarios.controller;

import com.smartlogix.usuarios.dto.request.SolicitudRol;
import com.smartlogix.usuarios.dto.response.RespuestaRol;
import com.smartlogix.usuarios.service.ServicioRol;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class ControladorRol {

    private final ServicioRol servicioRol;

    public ControladorRol(ServicioRol servicioRol) {
        this.servicioRol = servicioRol;
    }

    @PostMapping
    public ResponseEntity<RespuestaRol> crear(@Valid @RequestBody SolicitudRol solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioRol.crear(solicitud));
    }

    @GetMapping
    public ResponseEntity<List<RespuestaRol>> listar() {
        return ResponseEntity.ok(servicioRol.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaRol> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(servicioRol.obtenerPorId(id));
    }
}
