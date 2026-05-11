package com.example.ms_pedidos_smartlogix.dto;

public class CambiarEstadoPedidoRequestDTO {
    private String nuevoEstado;
    private String usuarioResponsable;
    private String observacion;

    public CambiarEstadoPedidoRequestDTO() {
    }

    public CambiarEstadoPedidoRequestDTO(String nuevoEstado, String usuarioResponsable, String observacion) {
        this.nuevoEstado = nuevoEstado;
        this.usuarioResponsable = usuarioResponsable;
        this.observacion = observacion;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public String getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(String usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
