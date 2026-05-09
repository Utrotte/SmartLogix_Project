package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.request.LoginRequest;
import com.smartlogix.bff.dto.request.LogoutRequest;
import com.smartlogix.bff.dto.response.LoginResponse;
import com.smartlogix.bff.dto.response.UsuarioResponse;
import com.smartlogix.bff.dto.response.ApiResponse;
import com.smartlogix.bff.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        
        String ipOrigen = obtenerIpOrigen(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        
        LoginResponse response = authService.login(request, ipOrigen, userAgent);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            @RequestHeader("X-Session-Token") String token) {
        
        authService.logout(token);
        
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Logout exitoso")
                .data(null)
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> obtenerUsuarioActual(
            @RequestHeader("X-Session-Token") String token) {
        
        UsuarioResponse response = authService.obtenerUsuarioActual(token);
        return ResponseEntity.ok(response);
    }

    private String obtenerIpOrigen(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            return ip.split(",")[0];
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty()) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
