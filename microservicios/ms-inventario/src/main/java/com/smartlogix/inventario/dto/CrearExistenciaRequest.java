package com.smartlogix.inventario.dto;

import jakarta.validation.constraints.NotNull;

public class CrearExistenciaRequest {

    @NotNull
    private Long idProducto;
    @NotNull
    private Long idBodega;
    @NotNull
    private Integer stockActual;

    public CrearExistenciaRequest() {}
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public Long getIdBodega() { return idBodega; }
    public void setIdBodega(Long idBodega) { this.idBodega = idBodega; }
    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
}
