package com.smartlogix.bff.security;

import com.smartlogix.bff.repository.SesionAplicacionRepository;
import com.smartlogix.bff.model.SesionAplicacion;
import com.smartlogix.bff.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionTokenFilter extends OncePerRequestFilter {

    private final SesionAplicacionRepository sesionRepository;
    
    // Rutas públicas que no requieren token
    private static final String[] PUBLIC_URLS = {
            "/api/auth/login",
            "/api/bff/ping",
            "/swagger-ui",
            "/v3/api-docs",
            "/actuator/health"
    };

    public SessionTokenFilter(SesionAplicacionRepository sesionRepository) {
        this.sesionRepository = sesionRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        String requestPath = request.getServletPath();
        
        System.out.println("SessionTokenFilter -> URI: " + request.getRequestURI());
        System.out.println("SessionTokenFilter -> Method: " + request.getMethod());

        // Permitir rutas públicas
        if (isPublicUrl(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Permitir OPTIONS (preflight CORS)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Obtener token del header
        String token = request.getHeader("X-Session-Token");
        
        if (token == null || token.isEmpty()) {
            System.out.println("SessionTokenFilter -> No llegó X-Session-Token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"No autenticado: Falla X-Session-Token\"}");
            response.setContentType("application/json");
            return;
        }
        
        System.out.println("SessionTokenFilter -> Token recibido: " + token);

        try {
            // Buscar sesión activa
            Optional<SesionAplicacion> sesionOpt = sesionRepository
                    .findByTokenReferenciaAndEstado(token, "ACTIVA");
            
            if (sesionOpt.isEmpty()) {
                System.out.println("SessionTokenFilter -> Token no encontrado o no activo");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Token inválido o inactivo\"}");
                response.setContentType("application/json");
                return;
            }
            
            SesionAplicacion sesion = sesionOpt.get();
            
            // Validar que no haya expirado
            if (LocalDateTime.now().isAfter(sesion.getFechaExpiracion())) {
                System.out.println("SessionTokenFilter -> Token expirado");
                sesion.setEstado("EXPIRADA");
                sesionRepository.save(sesion);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Token expirado\"}");
                response.setContentType("application/json");
                return;
            }
            
            System.out.println("SessionTokenFilter -> Usuario autenticado: " + sesion.getUsuario().getCorreo());

            // Cargar roles del usuario
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (sesion.getUsuario() != null && sesion.getUsuario().getUsuarioRoles() != null) {
                sesion.getUsuario().getUsuarioRoles().forEach(ur ->
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + ur.getRol().getNombreRol()))
                );
            }
            
            // Crear Authentication
            Authentication auth = new BffAuthentication(
                    sesion.getUsuario().getIdUsuario(),
                    sesion.getUsuario().getCorreo(),
                    token,
                    authorities
            );
            
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            
        } catch (Exception e) {
            logger.error("Error al procesar token de sesión", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Error interno al validar token\"}");
            response.setContentType("application/json");
            return;
        }
        
        filterChain.doFilter(request, response);
    }

    private boolean isPublicUrl(String path) {
        for (String publicUrl : PUBLIC_URLS) {
            if (path.startsWith(publicUrl)) {
                return true;
            }
        }
        return false;
    }
}
