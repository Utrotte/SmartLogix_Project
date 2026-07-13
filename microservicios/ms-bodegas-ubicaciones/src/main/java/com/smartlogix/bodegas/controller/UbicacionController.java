package com.smartlogix.bodegas.controller;

import com.smartlogix.bodegas.dto.UbicacionRequestDTO;
import com.smartlogix.bodegas.dto.UbicacionResponseDTO;
import com.smartlogix.bodegas.service.BodegaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionController {

    private final BodegaService bodegaService;

    public UbicacionController(BodegaService bodegaService) {
        this.bodegaService = bodegaService;
    }

    @GetMapping("/bodega/{idBodega}")
    public List<UbicacionResponseDTO> listarPorBodega(@PathVariable Long idBodega) {
        return bodegaService.listarUbicacionesPorBodega(idBodega);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UbicacionResponseDTO crear(@Valid @RequestBody UbicacionRequestDTO req) {
        return bodegaService.crearUbicacion(req);
    }
}
