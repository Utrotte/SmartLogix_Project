package com.example.ms_pedidos_smartlogix.integration.dto;

import java.time.LocalDateTime;

public class ReservaInventarioResponseDTO {

    private Long idReserva;
    private Long idPedidoRef;
    private String codigoPedidoRef;
    private String estadoReserva;
    private LocalDateTime fechaReserva;
    private LocalDateTime fechaExpiracion;
    private String mensaje;

    public ReservaInventarioResponseDTO() {
    }

    public ReservaInventarioResponseDTO(Long idReserva, Long idPedidoRef, String codigoPedidoRef,
                                        String estadoReserva, LocalDateTime fechaReserva,
                                        LocalDateTime fechaExpiracion, String mensaje) {
        this.idReserva = idReserva;
        this.idPedidoRef = idPedidoRef;
        this.codigoPedidoRef = codigoPedidoRef;
        this.estadoReserva = estadoReserva;
        this.fechaReserva = fechaReserva;
        this.fechaExpiracion = fechaExpiracion;
        this.mensaje = mensaje;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public Long getIdPedidoRef() {
        return idPedidoRef;
    }

    public void setIdPedidoRef(Long idPedidoRef) {
        this.idPedidoRef = idPedidoRef;
    }

    public String getCodigoPedidoRef() {
        return codigoPedidoRef;
    }

    public void setCodigoPedidoRef(String codigoPedidoRef) {
        this.codigoPedidoRef = codigoPedidoRef;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
