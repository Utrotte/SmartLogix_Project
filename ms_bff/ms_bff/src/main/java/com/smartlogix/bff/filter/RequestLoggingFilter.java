package com.smartlogix.bff.filter;

import com.smartlogix.bff.service.BitacoraSolicitudService;
import com.smartlogix.bff.security.BffAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final BitacoraSolicitudService bitacoraService;

    public RequestLoggingFilter(BitacoraSolicitudService bitacoraService) {
        this.bitacoraService = bitacoraService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {
        
        long startTime = System.currentTimeMillis();
        String correlationId = request.getHeader("X-Correlation-Id");
        
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }
        
        response.setHeader("X-Correlation-Id", correlationId);
        
        try {
            filterChain.doFilter(request, response);
        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            // No registrar archivos estáticos
            String requestPath = request.getServletPath();
            if (!requestPath.startsWith("/static") && !requestPath.startsWith("/css") && 
                !requestPath.startsWith("/js") && !requestPath.startsWith("/images")) {
                
                Long idUsuario = null;
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                
                if (auth != null && auth instanceof BffAuthentication) {
                    BffAuthentication bffAuth = (BffAuthentication) auth;
                    idUsuario = bffAuth.getUserId();
                }
                
                String servicioDestino = detectarServicio(requestPath);
                String ipOrigen = obtenerIpOrigen(request);
                String userAgent = request.getHeader("User-Agent");
                
                try {
                    bitacoraService.registrarSolicitud(
                            idUsuario,
                            requestPath,
                            request.getMethod(),
                            servicioDestino,
                            response.getStatus(),
                            duration,
                            correlationId,
                            ipOrigen,
                            userAgent,
                            null
                    );
                } catch (Exception e) {
                    logger.error("Error al registrar bitácora", e);
                }
            }
        }
    }

    private String detectarServicio(String ruta) {
        if (ruta.contains("/inventario")) return "INVENTARIO";
        if (ruta.contains("/pedidos")) return "PEDIDOS";
        if (ruta.contains("/envios")) return "ENVIOS";
        if (ruta.contains("/auth")) return "AUTH";
        if (ruta.contains("/usuarios")) return "USUARIOS";
        if (ruta.contains("/roles")) return "ROLES";
        if (ruta.contains("/bitacora")) return "BITACORA";
        return "BFF";
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
