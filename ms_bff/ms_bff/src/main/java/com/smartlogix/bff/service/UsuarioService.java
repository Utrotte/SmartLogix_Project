package com.smartlogix.bff.service;

import com.smartlogix.bff.dto.request.ActualizarUsuarioRequest;
import com.smartlogix.bff.dto.request.CrearUsuarioRequest;
import com.smartlogix.bff.dto.response.UsuarioResponse;
import com.smartlogix.bff.exception.DuplicateResourceException;
import com.smartlogix.bff.exception.ResourceNotFoundException;
import com.smartlogix.bff.model.Usuario;
import com.smartlogix.bff.model.UsuarioRol;
import com.smartlogix.bff.model.Rol;
import com.smartlogix.bff.repository.UsuarioRepository;
import com.smartlogix.bff.repository.RolRepository;
import com.smartlogix.bff.repository.UsuarioRolRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                         RolRepository rolRepository,
                         UsuarioRolRepository usuarioRolRepository,
                         PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse crearUsuario(CrearUsuarioRequest request) {
        
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new DuplicateResourceException("El correo ya está registrado");
        }
        
        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .estado("ACTIVO")
                .build();
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        // Asignar roles
        for (String nombreRol : request.getRoles()) {
            Rol rol = rolRepository.findByNombreRol(nombreRol)
                    .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + nombreRol));
            
            UsuarioRol usuarioRol = UsuarioRol.builder()
                    .usuario(usuarioGuardado)
                    .rol(rol)
                    .fechaAsignacion(LocalDateTime.now())
                    .build();
            
            usuarioRolRepository.save(usuarioRol);
        }
        
        return mapToResponse(usuarioGuardado);
    }

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponse buscarPorId(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return mapToResponse(usuario);
    }

    @Transactional
    public UsuarioResponse actualizarUsuario(Long idUsuario, ActualizarUsuarioRequest request) {
        
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        usuario.setNombre(request.getNombre());
        usuario.setEstado(request.getEstado());
        
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        
        // Actualizar roles si se proporcionan
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            usuarioRolRepository.deleteAll(usuarioActualizado.getUsuarioRoles());
            
            for (String nombreRol : request.getRoles()) {
                Rol rol = rolRepository.findByNombreRol(nombreRol)
                        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + nombreRol));
                
                UsuarioRol usuarioRol = UsuarioRol.builder()
                        .usuario(usuarioActualizado)
                        .rol(rol)
                        .fechaAsignacion(LocalDateTime.now())
                        .build();
                
                usuarioRolRepository.save(usuarioRol);
            }
        }
        
        return mapToResponse(usuarioActualizado);
    }

    @Transactional
    public void desactivarUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        usuario.setEstado("INACTIVO");
        usuarioRepository.save(usuario);
    }

    private UsuarioResponse mapToResponse(Usuario usuario) {
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
}
