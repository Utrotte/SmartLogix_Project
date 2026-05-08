package com.example.ms_pedidos_smartlogix.integration.dto;

public class ReservaInventarioDetalleRequestDTO {

    private Long idProducto;
    private Long idBodega;
    private Integer cantidadReservada;

    public ReservaInventarioDetalleRequestDTO() {
    }

    public ReservaInventarioDetalleRequestDTO(Long idProducto, Long idBodega, Integer cantidadReservada) {
        this.idProducto = idProducto;
        this.idBodega = idBodega;
        this.cantidadReservada = cantidadReservada;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getIdBodega() {
        return idBodega;
    }

    public void setIdBodega(Long idBodega) {
        this.idBodega = idBodega;
    }

    public Integer getCantidadReservada() {
        return cantidadReservada;
    }

    public void setCantidadReservada(Integer cantidadReservada) {
        this.cantidadReservada = cantidadReservada;
    }
}
