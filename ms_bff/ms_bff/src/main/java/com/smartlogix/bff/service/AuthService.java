package com.smartlogix.bff.service;

import com.smartlogix.bff.dto.request.LoginRequest;
import com.smartlogix.bff.dto.response.LoginResponse;
import com.smartlogix.bff.dto.response.UsuarioResponse;
import com.smartlogix.bff.exception.BusinessException;
import com.smartlogix.bff.exception.ResourceNotFoundException;
import com.smartlogix.bff.model.Usuario;
import com.smartlogix.bff.model.SesionAplicacion;
import com.smartlogix.bff.repository.UsuarioRepository;
import com.smartlogix.bff.repository.SesionAplicacionRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final SesionAplicacionRepository sesionRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,
                      SesionAplicacionRepository sesionRepository,
                      PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.sesionRepository = sesionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public LoginResponse login(LoginRequest request, String ipOrigen, String userAgent) {
        
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        if (!"ACTIVO".equals(usuario.getEstado())) {
            throw new BusinessException("Usuario no activo");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new BusinessException("Contraseña incorrecta");
        }
        
        // Generar token
        String token = UUID.randomUUID().toString();
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime expiracion = ahora.plusMinutes(120);
        
        // Crear sesión
        SesionAplicacion sesion = SesionAplicacion.builder()
                .usuario(usuario)
                .tokenReferencia(token)
                .fechaInicio(ahora)
                .fechaExpiracion(expiracion)
                .estado("ACTIVA")
                .ipOrigen(ipOrigen)
                .userAgent(userAgent)
                .build();
        
        sesionRepository.saveAndFlush(sesion);
        
        // Construir respuesta
        List<String> roles = usuario.getUsuarioRoles().stream()
                .map(ur -> ur.getRol().getNombreRol())
                .collect(Collectors.toList());
        
        return LoginResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .roles(roles)
                .tokenReferencia(token)
                .fechaExpiracion(expiracion)
                .mensaje("Login exitoso")
                .build();
    }

    @Transactional
    public void logout(String tokenReferencia) {
        SesionAplicacion sesion = sesionRepository.findByTokenReferenciaAndEstado(tokenReferencia, "ACTIVA")
                .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada"));
        
        sesion.setEstado("CERRADA");
        sesionRepository.save(sesion);
    }

    public UsuarioResponse obtenerUsuarioActual(String tokenReferencia) {
        SesionAplicacion sesion = sesionRepository.findByTokenReferenciaAndEstado(tokenReferencia, "ACTIVA")
                .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada"));
        
        Usuario usuario = sesion.getUsuario();
        
        List<String> roles = usuario.getUsuarioRoles().stream()
                .map(ur -> ur.getRol().getNombreRol())
                .collect(Collectors.toList());
        
        return UsuarioResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .estado(usuario.getEstado())
                .roles(roles)
                .fechaCreacion(usuario.getFechaCreacion())
                .build();
    }

    public boolean validarToken(String tokenReferencia) {
        if (tokenReferencia == null || tokenReferencia.isEmpty()) {
            return false;
        }
        
        boolean exists = sesionRepository.existsByTokenReferenciaAndEstado(tokenReferencia, "ACTIVA");
        
        if (exists) {
            SesionAplicacion sesion = sesionRepository.findByTokenReferenciaAndEstado(tokenReferencia, "ACTIVA")
                    .orElse(null);
            
            if (sesion != null && LocalDateTime.now().isAfter(sesion.getFechaExpiracion())) {
                sesion.setEstado("EXPIRADA");
                sesionRepository.save(sesion);
                return false;
            }
        }
        
        return exists;
    }
}
