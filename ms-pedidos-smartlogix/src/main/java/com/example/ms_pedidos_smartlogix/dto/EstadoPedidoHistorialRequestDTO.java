package com.example.ms_pedidos_smartlogix.dto;

public class EstadoPedidoHistorialRequestDTO {
    private Long idPedido;
    private String estado;
    private String observacion;
    private String usuarioResponsable;

    public EstadoPedidoHistorialRequestDTO() {
    }

    public EstadoPedidoHistorialRequestDTO(Long idPedido, String estado, String observacion, String usuarioResponsable) {
        this.idPedido = idPedido;
        this.estado = estado;
        this.observacion = observacion;
        this.usuarioResponsable = usuarioResponsable;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(String usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }
}
