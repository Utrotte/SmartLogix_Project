package com.example.ms_pedidos_smartlogix.dto;

public class CambiarEstadoPedidoRequestDTO {
    private String estado;
    private String usuarioResponsable;
    private String observacion;

    public CambiarEstadoPedidoRequestDTO() {
    }

    public CambiarEstadoPedidoRequestDTO(String estado, String usuarioResponsable, String observacion) {
        this.estado = estado;
        this.usuarioResponsable = usuarioResponsable;
        this.observacion = observacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
