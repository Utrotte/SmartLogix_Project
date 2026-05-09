package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.request.CrearRolRequest;
import com.smartlogix.bff.dto.response.RolResponse;
import com.smartlogix.bff.service.RolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @PostMapping("/")
    public ResponseEntity<RolResponse> crearRol(
            @Valid @RequestBody CrearRolRequest request) {
        
        RolResponse response = rolService.crearRol(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<RolResponse>> listarRoles() {
        List<RolResponse> roles = rolService.listarRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{idRol}")
    public ResponseEntity<RolResponse> buscarRol(
            @PathVariable("idRol") Long idRol) {
        
        RolResponse rol = rolService.buscarPorId(idRol);
        return ResponseEntity.ok(rol);
    }
}
