package com.smartlogix.bodegas.service;

import com.smartlogix.bodegas.domain.Bodega;
import com.smartlogix.bodegas.domain.UbicacionBodega;
import com.smartlogix.bodegas.domain.ZonaBodega;
import com.smartlogix.bodegas.dto.*;
import com.smartlogix.bodegas.repository.BodegaRepository;
import com.smartlogix.bodegas.repository.UbicacionBodegaRepository;
import com.smartlogix.bodegas.repository.ZonaBodegaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BodegaService {

    private final BodegaRepository bodegaRepository;
    private final ZonaBodegaRepository zonaRepository;
    private final UbicacionBodegaRepository ubicacionRepository;

    public BodegaService(BodegaRepository bodegaRepository, ZonaBodegaRepository zonaRepository, UbicacionBodegaRepository ubicacionRepository) {
        this.bodegaRepository = bodegaRepository;
        this.zonaRepository = zonaRepository;
        this.ubicacionRepository = ubicacionRepository;
    }

    public List<BodegaResponseDTO> listarBodegas() {
        return bodegaRepository.findAll().stream()
                .map(b -> new BodegaResponseDTO(b.getId(), b.getNombre(), b.getDireccion(), b.isActivo()))
                .collect(Collectors.toList());
    }

    public BodegaResponseDTO obtenerPorId(Long id) {
        Bodega b = bodegaRepository.findById(id).orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        return new BodegaResponseDTO(b.getId(), b.getNombre(), b.getDireccion(), b.isActivo());
    }

    @Transactional
    public BodegaResponseDTO crearBodega(BodegaRequestDTO req) {
        Bodega b = new Bodega(req.getNombre(), req.getDireccion());
        b = bodegaRepository.save(b);
        return new BodegaResponseDTO(b.getId(), b.getNombre(), b.getDireccion(), b.isActivo());
    }

    @Transactional
    public BodegaResponseDTO actualizarBodega(Long id, BodegaRequestDTO req) {
        Bodega b = bodegaRepository.findById(id).orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        b.setNombre(req.getNombre());
        b.setDireccion(req.getDireccion());
        b = bodegaRepository.save(b);
        return new BodegaResponseDTO(b.getId(), b.getNombre(), b.getDireccion(), b.isActivo());
    }

    @Transactional
    public void desactivarBodega(Long id) {
        Bodega b = bodegaRepository.findById(id).orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        b.setActivo(false);
        bodegaRepository.save(b);
    }

    public List<ZonaResponseDTO> listarZonasPorBodega(Long idBodega) {
        return zonaRepository.findByIdBodega(idBodega).stream()
                .map(z -> new ZonaResponseDTO(z.getId(), z.getIdBodega(), z.getNombre(), z.isActivo()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ZonaResponseDTO crearZonaEnBodega(Long idBodega, ZonaRequestDTO req) {
        bodegaRepository.findById(idBodega).orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        ZonaBodega z = new ZonaBodega(idBodega, req.getNombre());
        z = zonaRepository.save(z);
        return new ZonaResponseDTO(z.getId(), z.getIdBodega(), z.getNombre(), z.isActivo());
    }

    public List<UbicacionResponseDTO> listarUbicacionesPorBodega(Long idBodega) {
        return ubicacionRepository.findByIdBodega(idBodega).stream()
                .map(u -> new UbicacionResponseDTO(u.getId(), u.getIdBodega(), u.getIdZona(), u.getCodigo(), u.getEstado()))
                .collect(Collectors.toList());
    }

    @Transactional
    public UbicacionResponseDTO crearUbicacion(UbicacionRequestDTO req) {
        bodegaRepository.findById(req.getIdBodega()).orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        zonaRepository.findById(req.getIdZona()).orElseThrow(() -> new RuntimeException("Zona no encontrada"));
        UbicacionBodega u = new UbicacionBodega(req.getIdBodega(), req.getIdZona(), req.getCodigo(), req.getEstado());
        u = ubicacionRepository.save(u);
        return new UbicacionResponseDTO(u.getId(), u.getIdBodega(), u.getIdZona(), u.getCodigo(), u.getEstado());
    }
}
