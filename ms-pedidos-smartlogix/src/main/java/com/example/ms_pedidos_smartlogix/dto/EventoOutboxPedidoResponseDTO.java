package com.example.ms_pedidos_smartlogix.dto;

import java.time.LocalDateTime;

public class EventoOutboxPedidoResponseDTO {
    private Long idEvento;
    private Long idPedido;
    private String tipoEvento;
    private String payload;
    private String estadoPublicacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaPublicacion;

    public EventoOutboxPedidoResponseDTO() {
    }

    public EventoOutboxPedidoResponseDTO(Long idEvento, Long idPedido, String tipoEvento, String payload,
                                         String estadoPublicacion, LocalDateTime fechaCreacion, LocalDateTime fechaPublicacion) {
        this.idEvento = idEvento;
        this.idPedido = idPedido;
        this.tipoEvento = tipoEvento;
        this.payload = payload;
        this.estadoPublicacion = estadoPublicacion;
        this.fechaCreacion = fechaCreacion;
        this.fechaPublicacion = fechaPublicacion;
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getEstadoPublicacion() {
        return estadoPublicacion;
    }

    public void setEstadoPublicacion(String estadoPublicacion) {
        this.estadoPublicacion = estadoPublicacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }
}
