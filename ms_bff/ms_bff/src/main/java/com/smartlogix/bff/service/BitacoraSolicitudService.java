package com.smartlogix.bff.service;

import com.smartlogix.bff.dto.response.BitacoraSolicitudResponse;
import com.smartlogix.bff.model.BitacoraSolicitud;
import com.smartlogix.bff.repository.BitacoraSolicitudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BitacoraSolicitudService {

    private final BitacoraSolicitudRepository bitacoraRepository;

    public BitacoraSolicitudService(BitacoraSolicitudRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }

    @Transactional
    public void registrarSolicitud(Long idUsuario, String ruta, String metodoHttp,
                                   String servicioDestino, Integer estadoRespuesta,
                                   Long tiempoMs, String correlationId, String ipOrigen,
                                   String userAgent, String mensajeError) {
        
        BitacoraSolicitud bitacora = BitacoraSolicitud.builder()
                .idUsuario(idUsuario)
                .ruta(ruta)
                .metodoHttp(metodoHttp)
                .servicioDestino(servicioDestino)
                .estadoRespuesta(estadoRespuesta)
                .tiempoMs(tiempoMs)
                .correlationId(correlationId)
                .fechaSolicitud(LocalDateTime.now())
                .ipOrigen(ipOrigen)
                .userAgent(userAgent)
                .mensajeError(mensajeError)
                .build();
        
        bitacoraRepository.save(bitacora);
    }

    public List<BitacoraSolicitudResponse> listarBitacoras() {
        return bitacoraRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BitacoraSolicitudResponse> listarPorServicio(String servicioDestino) {
        return bitacoraRepository.findByServicioDestino(servicioDestino).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BitacoraSolicitudResponse> listarPorUsuario(Long idUsuario) {
        return bitacoraRepository.findByIdUsuario(idUsuario).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private BitacoraSolicitudResponse mapToResponse(BitacoraSolicitud bitacora) {
        return BitacoraSolicitudResponse.builder()
                .idBitacora(bitacora.getIdBitacora())
                .idUsuario(bitacora.getIdUsuario())
                .ruta(bitacora.getRuta())
                .metodoHttp(bitacora.getMetodoHttp())
                .servicioDestino(bitacora.getServicioDestino())
                .estadoRespuesta(bitacora.getEstadoRespuesta())
                .tiempoMs(bitacora.getTiempoMs())
                .correlationId(bitacora.getCorrelationId())
                .fechaSolicitud(bitacora.getFechaSolicitud())
                .mensajeError(bitacora.getMensajeError())
                .build();
    }
}
