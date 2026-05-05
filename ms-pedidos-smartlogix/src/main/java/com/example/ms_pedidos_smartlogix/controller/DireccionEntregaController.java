package com.example.ms_pedidos_smartlogix.controller;

import com.example.ms_pedidos_smartlogix.dto.DireccionEntregaRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.DireccionEntregaResponseDTO;
import com.example.ms_pedidos_smartlogix.service.DireccionEntregaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/direcciones-entrega")
public class DireccionEntregaController {

    private final DireccionEntregaService direccionEntregaService;

    public DireccionEntregaController(DireccionEntregaService direccionEntregaService) {
        this.direccionEntregaService = direccionEntregaService;
    }

    @PostMapping
    public ResponseEntity<DireccionEntregaResponseDTO> crearDireccion(@RequestBody DireccionEntregaRequestDTO dto) {
        DireccionEntregaResponseDTO response = direccionEntregaService.crearDireccion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DireccionEntregaResponseDTO>> listarDirecciones() {
        List<DireccionEntregaResponseDTO> direcciones = direccionEntregaService.listarDirecciones();
        return ResponseEntity.ok(direcciones);
    }

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<DireccionEntregaResponseDTO> obtenerDireccionPorPedido(@PathVariable Long idPedido) {
        DireccionEntregaResponseDTO direccion = direccionEntregaService.obtenerDireccionPorPedido(idPedido);
        return ResponseEntity.ok(direccion);
    }

    @PutMapping("/{idDireccion}")
    public ResponseEntity<DireccionEntregaResponseDTO> actualizarDireccion(
            @PathVariable Long idDireccion,
            @RequestBody DireccionEntregaRequestDTO dto) {
        DireccionEntregaResponseDTO response = direccionEntregaService.actualizarDireccion(idDireccion, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{idDireccion}")
    public ResponseEntity<Void> eliminarDireccion(@PathVariable Long idDireccion) {
        direccionEntregaService.eliminarDireccion(idDireccion);
        return ResponseEntity.noContent().build();
    }
}
