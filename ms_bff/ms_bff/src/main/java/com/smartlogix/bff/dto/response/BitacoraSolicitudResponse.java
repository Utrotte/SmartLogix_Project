package com.smartlogix.bff.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BitacoraSolicitudResponse {
    
    private Long idBitacora;
    private Long idUsuario;
    private String ruta;
    private String metodoHttp;
    private String servicioDestino;
    private Integer estadoRespuesta;
    private Long tiempoMs;
    private String correlationId;
    private LocalDateTime fechaSolicitud;
    private String mensajeError;
}
