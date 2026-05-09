package com.smartlogix.bff.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bitacora_solicitud")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BitacoraSolicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bitacora")
    private Long idBitacora;

    @Column(name = "id_usuario", nullable = true)
    private Long idUsuario;

    @Column(name = "ruta")
    private String ruta;

    @Column(name = "metodo_http")
    private String metodoHttp;

    @Column(name = "servicio_destino")
    private String servicioDestino;

    @Column(name = "estado_respuesta")
    private Integer estadoRespuesta;

    @Column(name = "tiempo_ms")
    private Long tiempoMs;

    @Column(name = "correlation_id")
    private String correlationId;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    @Column(name = "ip_origen")
    private String ipOrigen;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "mensaje_error", nullable = true, length = 1000)
    private String mensajeError;
}
