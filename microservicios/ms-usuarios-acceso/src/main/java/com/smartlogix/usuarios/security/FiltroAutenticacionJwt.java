package com.smartlogix.usuarios.security;

import com.smartlogix.usuarios.repository.RepositorioSesionUsuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtro que intercepta cada petición HTTP y valida el header Authorization: Bearer {token}.
 * Solo permite continuar si el token existe y la sesión sigue activa en bd_usuarios.
 */
@Component
public class FiltroAutenticacionJwt extends OncePerRequestFilter {

    private final ServicioJwt servicioJwt;
    private final RepositorioSesionUsuario repositorioSesion;

    public FiltroAutenticacionJwt(ServicioJwt servicioJwt, RepositorioSesionUsuario repositorioSesion) {
        this.servicioJwt = servicioJwt;
        this.repositorioSesion = repositorioSesion;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest peticion, HttpServletResponse respuesta, FilterChain cadena)
            throws ServletException, IOException {
        String encabezado = peticion.getHeader(HttpHeaders.AUTHORIZATION);
        if (encabezado != null && encabezado.startsWith("Bearer ")) {
            String token = encabezado.substring(7);
            repositorioSesion.findByTokenJwtAndActivaTrue(token).ifPresent(sesion -> {
                var claims = servicioJwt.obtenerClaims(token);
                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) claims.get("roles");
                var permisos = roles.stream()
                        .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol))
                        .collect(Collectors.toList());
                var autenticacion = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, permisos);
                SecurityContextHolder.getContext().setAuthentication(autenticacion);
            });
        }
        cadena.doFilter(peticion, respuesta);
    }
}
