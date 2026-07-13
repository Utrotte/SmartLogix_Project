package com.smartlogix.inventario.dto;

import jakarta.validation.constraints.NotNull;

public class CrearReservaRequest {
    @NotNull
    private Long idProducto;
    @NotNull
    private Long idBodega;
    @NotNull
    private Long idPedido;
    @NotNull
    private Integer cantidadReservada;

    public CrearReservaRequest() {}
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public Long getIdBodega() { return idBodega; }
    public void setIdBodega(Long idBodega) { this.idBodega = idBodega; }
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public Integer getCantidadReservada() { return cantidadReservada; }
    public void setCantidadReservada(Integer cantidadReservada) { this.cantidadReservada = cantidadReservada; }
}
