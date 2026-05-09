package com.smartlogix.bff.controller;

import com.smartlogix.bff.service.BffEnviosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bff/envios")
public class BffEnviosController {

    private final BffEnviosService enviosService;

    public BffEnviosController(BffEnviosService enviosService) {
        this.enviosService = enviosService;
    }

    @GetMapping("/")
    public ResponseEntity<List<?>> listarEnvios() {
        List<?> response = enviosService.listarEnvios();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/")
    public ResponseEntity<Object> crearEnvio(@RequestBody Map<String, Object> request) {
        Object response = enviosService.crearEnvio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{idEnvio}")
    public ResponseEntity<Object> buscarEnvio(@PathVariable("idEnvio") Long idEnvio) {
        Object response = enviosService.buscarEnvio(idEnvio);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idEnvio}")
    public ResponseEntity<Object> actualizarEnvio(
            @PathVariable("idEnvio") Long idEnvio,
            @RequestBody Map<String, Object> request) {
        Object response = enviosService.actualizarEnvio(idEnvio, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{idEnvio}/transportista")
    public ResponseEntity<Object> asignarTransportista(
            @PathVariable("idEnvio") Long idEnvio,
            @RequestBody Map<String, Object> request) {
        Object response = enviosService.asignarTransportista(idEnvio, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{idEnvio}/estado")
    public ResponseEntity<Object> actualizarEstadoEnvio(
            @PathVariable("idEnvio") Long idEnvio,
            @RequestBody Map<String, Object> request) {
        Object response = enviosService.actualizarEstadoEnvio(idEnvio, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{idEnvio}/seguimiento")
    public ResponseEntity<List<?>> listarSeguimiento(@PathVariable("idEnvio") Long idEnvio) {
        List<?> response = enviosService.listarSeguimiento(idEnvio);
        return ResponseEntity.ok(response);
    }
}
