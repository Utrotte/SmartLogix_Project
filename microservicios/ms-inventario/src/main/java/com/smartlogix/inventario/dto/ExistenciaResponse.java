package com.smartlogix.inventario.dto;

public class ExistenciaResponse {
    private Long id;
    private Long idProducto;
    private Long idBodega;
    private Integer stockActual;
    private Integer stockReservado;
    private Integer stockDisponible;
    private boolean activo;

    public ExistenciaResponse() {}

    public ExistenciaResponse(Long id, Long idProducto, Long idBodega, Integer stockActual, Integer stockReservado, boolean activo) {
        this.id = id; this.idProducto = idProducto; this.idBodega = idBodega; this.stockActual = stockActual; this.stockReservado = stockReservado; this.stockDisponible = (stockActual==null?0:stockActual)-(stockReservado==null?0:stockReservado); this.activo = activo;
    }

    public Long getId() { return id; }
    public Long getIdProducto() { return idProducto; }
    public Long getIdBodega() { return idBodega; }
    public Integer getStockActual() { return stockActual; }
    public Integer getStockReservado() { return stockReservado; }
    public Integer getStockDisponible() { return stockDisponible; }
    public boolean isActivo() { return activo; }
}
