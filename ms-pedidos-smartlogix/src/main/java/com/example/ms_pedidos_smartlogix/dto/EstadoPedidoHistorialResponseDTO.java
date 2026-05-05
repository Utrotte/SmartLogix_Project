package com.example.ms_pedidos_smartlogix.dto;

import java.time.LocalDateTime;

public class EstadoPedidoHistorialResponseDTO {
    private Long idEstadoHistorial;
    private Long idPedido;
    private String estado;
    private LocalDateTime fechaEstado;
    private String observacion;
    private String usuarioResponsable;

    public EstadoPedidoHistorialResponseDTO() {
    }

    public EstadoPedidoHistorialResponseDTO(Long idEstadoHistorial, Long idPedido, String estado,
                                             LocalDateTime fechaEstado, String observacion, String usuarioResponsable) {
        this.idEstadoHistorial = idEstadoHistorial;
        this.idPedido = idPedido;
        this.estado = estado;
        this.fechaEstado = fechaEstado;
        this.observacion = observacion;
        this.usuarioResponsable = usuarioResponsable;
    }

    public Long getIdEstadoHistorial() {
        return idEstadoHistorial;
    }

    public void setIdEstadoHistorial(Long idEstadoHistorial) {
        this.idEstadoHistorial = idEstadoHistorial;
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

    public LocalDateTime getFechaEstado() {
        return fechaEstado;
    }

    public void setFechaEstado(LocalDateTime fechaEstado) {
        this.fechaEstado = fechaEstado;
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
