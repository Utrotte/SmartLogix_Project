package com.smartlogix.bff.service;

import com.smartlogix.bff.dto.request.CrearRolRequest;
import com.smartlogix.bff.dto.response.RolResponse;
import com.smartlogix.bff.exception.DuplicateResourceException;
import com.smartlogix.bff.exception.ResourceNotFoundException;
import com.smartlogix.bff.model.Rol;
import com.smartlogix.bff.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional
    public RolResponse crearRol(CrearRolRequest request) {
        
        if (rolRepository.existsByNombreRol(request.getNombreRol())) {
            throw new DuplicateResourceException("El rol ya existe: " + request.getNombreRol());
        }
        
        Rol rol = Rol.builder()
                .nombreRol(request.getNombreRol())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo() != null ? request.getActivo() : true)
                .build();
        
        Rol rolGuardado = rolRepository.save(rol);
        return mapToResponse(rolGuardado);
    }

    public List<RolResponse> listarRoles() {
        return rolRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RolResponse buscarPorId(Long idRol) {
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
        return mapToResponse(rol);
    }

    private RolResponse mapToResponse(Rol rol) {
        return RolResponse.builder()
                .idRol(rol.getIdRol())
                .nombreRol(rol.getNombreRol())
                .descripcion(rol.getDescripcion())
                .activo(rol.getActivo())
                .build();
    }
}
