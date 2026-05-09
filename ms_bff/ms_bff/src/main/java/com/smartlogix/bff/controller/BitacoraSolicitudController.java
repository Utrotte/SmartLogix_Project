package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.response.BitacoraSolicitudResponse;
import com.smartlogix.bff.service.BitacoraSolicitudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bitacora")
public class BitacoraSolicitudController {

    private final BitacoraSolicitudService bitacoraService;

    public BitacoraSolicitudController(BitacoraSolicitudService bitacoraService) {
        this.bitacoraService = bitacoraService;
    }

    @GetMapping("/")
    public ResponseEntity<List<BitacoraSolicitudResponse>> listarBitacoras() {
        List<BitacoraSolicitudResponse> bitacoras = bitacoraService.listarBitacoras();
        return ResponseEntity.ok(bitacoras);
    }

    @GetMapping("/servicio/{servicioDestino}")
    public ResponseEntity<List<BitacoraSolicitudResponse>> listarPorServicio(
            @PathVariable("servicioDestino") String servicioDestino) {
        
        List<BitacoraSolicitudResponse> bitacoras = bitacoraService.listarPorServicio(servicioDestino);
        return ResponseEntity.ok(bitacoras);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<BitacoraSolicitudResponse>> listarPorUsuario(
            @PathVariable("idUsuario") Long idUsuario) {
        
        List<BitacoraSolicitudResponse> bitacoras = bitacoraService.listarPorUsuario(idUsuario);
        return ResponseEntity.ok(bitacoras);
    }
}
