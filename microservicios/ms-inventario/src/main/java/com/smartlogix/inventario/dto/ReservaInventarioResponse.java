package com.smartlogix.inventario.dto;

import com.smartlogix.inventario.domain.enums.EstadoReserva;
import java.time.LocalDateTime;

public class ReservaInventarioResponse {
    private Long id;
    private Long idProducto;
    private Long idBodega;
    private Long idPedido;
    private Integer cantidadReservada;
    private EstadoReserva estadoReserva;
    private LocalDateTime fechaReserva;

    public ReservaInventarioResponse() {}
    public ReservaInventarioResponse(Long id, Long idProducto, Long idBodega, Long idPedido, Integer cantidadReservada, EstadoReserva estadoReserva, LocalDateTime fechaReserva) {
        this.id = id; this.idProducto = idProducto; this.idBodega = idBodega; this.idPedido = idPedido; this.cantidadReservada = cantidadReservada; this.estadoReserva = estadoReserva; this.fechaReserva = fechaReserva;
    }

    public Long getId() { return id; }
    public Long getIdProducto() { return idProducto; }
    public Long getIdBodega() { return idBodega; }
    public Long getIdPedido() { return idPedido; }
    public Integer getCantidadReservada() { return cantidadReservada; }
    public EstadoReserva getEstadoReserva() { return estadoReserva; }
    public LocalDateTime getFechaReserva() { return fechaReserva; }
}
