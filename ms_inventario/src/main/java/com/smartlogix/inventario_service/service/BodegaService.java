package com.smartlogix.inventario_service.service;

import com.smartlogix.inventario_service.dto.request.BodegaRequest;
import com.smartlogix.inventario_service.dto.response.BodegaResponse;
import com.smartlogix.inventario_service.exception.ResourceNotFoundException;
import com.smartlogix.inventario_service.model.Bodega;
import com.smartlogix.inventario_service.repository.BodegaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional // Todas las operaciones son transaccionales
public class BodegaService {

    private final BodegaRepository bodegaRepository;

    // Lectura: obtener bodega por ID
    @Transactional(readOnly = true)
    public BodegaResponse obtenerPorId(Long id) {
        return bodegaRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + id));
    }

    // Lectura: listar todas las bodegas
    @Transactional(readOnly = true)
    public List<BodegaResponse> listarTodas() {
        return bodegaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Crear nueva bodega
    public BodegaResponse crear(BodegaRequest request) {
        Bodega bodega = Bodega.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .comuna(request.getComuna())
                .ciudad(request.getCiudad())
                .region(request.getRegion())
                .activa(request.getActiva() != null ? request.getActiva() : true)
                .build();

        Bodega bodegaSaved = bodegaRepository.save(bodega);
        return mapToResponse(bodegaSaved);
    }

    // Actualizar bodega existente
    public BodegaResponse actualizar(Long id, BodegaRequest request) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + id));

        bodega.setNombre(request.getNombre());
        bodega.setDireccion(request.getDireccion());
        bodega.setComuna(request.getComuna());
        bodega.setCiudad(request.getCiudad());
        bodega.setRegion(request.getRegion());
        if (request.getActiva() != null) {
            bodega.setActiva(request.getActiva());
        }

        Bodega bodegaUpdated = bodegaRepository.save(bodega);
        return mapToResponse(bodegaUpdated);
    }

    // Desactivar bodega (baja lógica)
    public BodegaResponse desactivar(Long id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + id));

        bodega.setActiva(false);
        Bodega bodegaUpdated = bodegaRepository.save(bodega);
        return mapToResponse(bodegaUpdated);
    }

    // Convertir entity a DTO
    private BodegaResponse mapToResponse(Bodega bodega) {
        return BodegaResponse.builder()
                .idBodega(bodega.getIdBodega())
                .nombre(bodega.getNombre())
                .direccion(bodega.getDireccion())
                .comuna(bodega.getComuna())
                .ciudad(bodega.getCiudad())
                .region(bodega.getRegion())
                .activa(bodega.getActiva())
                .build();
    }
}
