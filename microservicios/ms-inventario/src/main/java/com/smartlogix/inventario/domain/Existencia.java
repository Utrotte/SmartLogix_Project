package com.smartlogix.inventario.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "existencias")
public class Existencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idProducto;

    private Long idBodega;

    private Integer stockActual = 0;

    private Integer stockReservado = 0;

    private boolean activo = true;

    public Existencia() {}

    public Existencia(Long idProducto, Long idBodega, Integer stockActual) {
        this.idProducto = idProducto;
        this.idBodega = idBodega;
        this.stockActual = stockActual != null ? stockActual : 0;
        this.stockReservado = 0;
        this.activo = true;
    }

    public Long getId() { return id; }
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public Long getIdBodega() { return idBodega; }
    public void setIdBodega(Long idBodega) { this.idBodega = idBodega; }
    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
    public Integer getStockReservado() { return stockReservado; }
    public void setStockReservado(Integer stockReservado) { this.stockReservado = stockReservado; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public int getStockDisponible() { return (stockActual == null ? 0 : stockActual) - (stockReservado == null ? 0 : stockReservado); }
}
