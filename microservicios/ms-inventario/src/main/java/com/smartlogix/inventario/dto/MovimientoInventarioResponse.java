package com.smartlogix.inventario.dto;

import com.smartlogix.inventario.domain.enums.TipoMovimiento;
import java.time.LocalDateTime;

public class MovimientoInventarioResponse {
    private Long id;
    private Long idProducto;
    private Long idBodega;
    private TipoMovimiento tipoMovimiento;
    private Integer cantidad;
    private String motivo;
    private LocalDateTime fechaMovimiento;

    public MovimientoInventarioResponse() {}
    public MovimientoInventarioResponse(Long id, Long idProducto, Long idBodega, TipoMovimiento tipoMovimiento, Integer cantidad, String motivo, LocalDateTime fechaMovimiento) {
        this.id = id; this.idProducto = idProducto; this.idBodega = idBodega; this.tipoMovimiento = tipoMovimiento; this.cantidad = cantidad; this.motivo = motivo; this.fechaMovimiento = fechaMovimiento;
    }

    public Long getId() { return id; }
    public Long getIdProducto() { return idProducto; }
    public Long getIdBodega() { return idBodega; }
    public TipoMovimiento getTipoMovimiento() { return tipoMovimiento; }
    public Integer getCantidad() { return cantidad; }
    public String getMotivo() { return motivo; }
    public LocalDateTime getFechaMovimiento() { return fechaMovimiento; }
}
