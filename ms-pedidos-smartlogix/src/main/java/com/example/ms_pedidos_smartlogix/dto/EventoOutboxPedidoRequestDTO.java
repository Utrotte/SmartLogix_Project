package com.example.ms_pedidos_smartlogix.dto;

public class EventoOutboxPedidoRequestDTO {
    private Long idPedido;
    private String tipoEvento;
    private String payload;
    private String estadoPublicacion;

    public EventoOutboxPedidoRequestDTO() {
    }

    public EventoOutboxPedidoRequestDTO(Long idPedido, String tipoEvento, String payload, String estadoPublicacion) {
        this.idPedido = idPedido;
        this.tipoEvento = tipoEvento;
        this.payload = payload;
        this.estadoPublicacion = estadoPublicacion;
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
}
