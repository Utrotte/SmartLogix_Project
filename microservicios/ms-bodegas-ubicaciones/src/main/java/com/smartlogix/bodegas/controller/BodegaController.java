package com.smartlogix.bodegas.controller;

import com.smartlogix.bodegas.dto.*;
import com.smartlogix.bodegas.service.BodegaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bodegas")
public class BodegaController {

    private final BodegaService bodegaService;

    public BodegaController(BodegaService bodegaService) {
        this.bodegaService = bodegaService;
    }

    @GetMapping
    public List<BodegaResponseDTO> listar() {
        return bodegaService.listarBodegas();
    }

    @GetMapping("/{id}")
    public BodegaResponseDTO obtener(@PathVariable Long id) {
        return bodegaService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BodegaResponseDTO crear(@Valid @RequestBody BodegaRequestDTO req) {
        return bodegaService.crearBodega(req);
    }

    @PutMapping("/{id}")
    public BodegaResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody BodegaRequestDTO req) {
        return bodegaService.actualizarBodega(id, req);
    }

    @PatchMapping("/{id}/desactivar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desactivar(@PathVariable Long id) {
        bodegaService.desactivarBodega(id);
    }

    @GetMapping("/{idBodega}/zonas")
    public List<ZonaResponseDTO> listarZonas(@PathVariable Long idBodega) {
        return bodegaService.listarZonasPorBodega(idBodega);
    }

    @PostMapping("/{idBodega}/zonas")
    @ResponseStatus(HttpStatus.CREATED)
    public ZonaResponseDTO crearZona(@PathVariable Long idBodega, @Valid @RequestBody ZonaRequestDTO req) {
        return bodegaService.crearZonaEnBodega(idBodega, req);
    }
}
