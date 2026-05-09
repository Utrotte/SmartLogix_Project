package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.request.ActualizarUsuarioRequest;
import com.smartlogix.bff.dto.request.CrearUsuarioRequest;
import com.smartlogix.bff.dto.response.UsuarioResponse;
import com.smartlogix.bff.dto.response.ApiResponse;
import com.smartlogix.bff.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/")
    public ResponseEntity<UsuarioResponse> crearUsuario(
            @Valid @RequestBody CrearUsuarioRequest request) {
        
        UsuarioResponse response = usuarioService.crearUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> buscarUsuario(
            @PathVariable("idUsuario") Long idUsuario) {
        
        UsuarioResponse usuario = usuarioService.buscarPorId(idUsuario);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(
            @PathVariable("idUsuario") Long idUsuario,
            @Valid @RequestBody ActualizarUsuarioRequest request) {
        
        UsuarioResponse usuario = usuarioService.actualizarUsuario(idUsuario, request);
        return ResponseEntity.ok(usuario);
    }

    @PatchMapping("/{idUsuario}/desactivar")
    public ResponseEntity<ApiResponse> desactivarUsuario(
            @PathVariable("idUsuario") Long idUsuario) {
        
        usuarioService.desactivarUsuario(idUsuario);
        
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Usuario desactivado exitosamente")
                .data(null)
                .build());
    }
}
