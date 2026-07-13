package com.smartlogix.usuarios.controller;

import com.smartlogix.usuarios.dto.request.SolicitudInicioSesion;
import com.smartlogix.usuarios.dto.response.RespuestaInicioSesion;
import com.smartlogix.usuarios.dto.response.RespuestaUsuario;
import com.smartlogix.usuarios.service.ServicioAutenticacion;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Endpoints de autenticación según README SmartLogix.
 * El API Gateway/BFF redirigirá aquí el login del frontend.
 */
@RestController
@RequestMapping("/api/autenticacion")
public class ControladorAutenticacion {

    private final ServicioAutenticacion servicioAutenticacion;

    public ControladorAutenticacion(ServicioAutenticacion servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;
    }

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<RespuestaInicioSesion> iniciarSesion(
            @Valid @RequestBody SolicitudInicioSesion solicitud,
            HttpServletRequest peticionHttp) {
        return ResponseEntity.ok(servicioAutenticacion.iniciarSesion(
                solicitud, obtenerIp(peticionHttp), peticionHttp.getHeader("User-Agent")));
    }

    @PostMapping("/cerrar-sesion")
    public ResponseEntity<Map<String, Object>> cerrarSesion(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String encabezadoAuth) {
        servicioAutenticacion.cerrarSesion(extraerToken(encabezadoAuth));
        return ResponseEntity.ok(Map.of("exito", true, "mensaje", "Sesión cerrada correctamente"));
    }

    @GetMapping("/usuario-actual")
    public ResponseEntity<RespuestaUsuario> usuarioActual(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String encabezadoAuth) {
        return ResponseEntity.ok(servicioAutenticacion.obtenerUsuarioActual(extraerToken(encabezadoAuth)));
    }

    private String extraerToken(String encabezadoAuth) {
        if (encabezadoAuth != null && encabezadoAuth.startsWith("Bearer ")) {
            return encabezadoAuth.substring(7);
        }
        throw new IllegalArgumentException("Se requiere token Bearer en el header Authorization");
    }

    private String obtenerIp(HttpServletRequest peticion) {
        String ip = peticion.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) {
            return ip.split(",")[0];
        }
        ip = peticion.getHeader("X-Real-IP");
        return (ip != null && !ip.isBlank()) ? ip : peticion.getRemoteAddr();
    }
}
