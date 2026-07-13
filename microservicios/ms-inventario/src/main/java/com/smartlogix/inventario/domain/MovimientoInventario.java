package com.smartlogix.inventario.domain;

import com.smartlogix.inventario.domain.enums.TipoMovimiento;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idProducto;

    private Long idBodega;

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipoMovimiento;

    private Integer cantidad;

    private String motivo;

    private LocalDateTime fechaMovimiento = LocalDateTime.now();

    public MovimientoInventario() {}

    public MovimientoInventario(Long idProducto, Long idBodega, TipoMovimiento tipoMovimiento, Integer cantidad, String motivo) {
        this.idProducto = idProducto;
        this.idBodega = idBodega;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.motivo = motivo;
    }

    public Long getId() { return id; }
    public Long getIdProducto() { return idProducto; }
    public Long getIdBodega() { return idBodega; }
    public TipoMovimiento getTipoMovimiento() { return tipoMovimiento; }
    public Integer getCantidad() { return cantidad; }
    public String getMotivo() { return motivo; }
    public LocalDateTime getFechaMovimiento() { return fechaMovimiento; }
}
