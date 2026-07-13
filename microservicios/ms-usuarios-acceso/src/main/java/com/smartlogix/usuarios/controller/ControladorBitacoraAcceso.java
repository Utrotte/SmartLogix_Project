package com.smartlogix.usuarios.controller;

import com.smartlogix.usuarios.dto.response.RespuestaBitacoraAcceso;
import com.smartlogix.usuarios.service.ServicioBitacoraAcceso;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bitacora-acceso")
public class ControladorBitacoraAcceso {

    private final ServicioBitacoraAcceso servicioBitacora;

    public ControladorBitacoraAcceso(ServicioBitacoraAcceso servicioBitacora) {
        this.servicioBitacora = servicioBitacora;
    }

    @GetMapping
    public ResponseEntity<List<RespuestaBitacoraAcceso>> listar() {
        return ResponseEntity.ok(servicioBitacora.listar());
    }
}
