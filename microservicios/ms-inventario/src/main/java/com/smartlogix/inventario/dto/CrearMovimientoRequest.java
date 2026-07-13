package com.smartlogix.inventario.dto;

import com.smartlogix.inventario.domain.enums.TipoMovimiento;
import jakarta.validation.constraints.NotNull;

public class CrearMovimientoRequest {

    @NotNull
    private Long idProducto;
    @NotNull
    private Long idBodega;
    @NotNull
    private TipoMovimiento tipoMovimiento;
    @NotNull
    private Integer cantidad;
    private String motivo;

    public CrearMovimientoRequest() {}
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public Long getIdBodega() { return idBodega; }
    public void setIdBodega(Long idBodega) { this.idBodega = idBodega; }
    public TipoMovimiento getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
