package cl.programadormaldito.ms_envios.integration.dto;

public class ExistenciaResponseDTO {

    private Long idExistencia;
    private Long idProductoRef;
    private Long idBodegaRef;
    private Integer stockActual;
    private Integer stockReservado;
    private Integer stockDisponible;
    private Integer stockMinimo;

    public ExistenciaResponseDTO() {}

    public Long getIdExistencia() { return idExistencia; }
    public void setIdExistencia(Long idExistencia) { this.idExistencia = idExistencia; }

    public Long getIdProductoRef() { return idProductoRef; }
    public void setIdProductoRef(Long idProductoRef) { this.idProductoRef = idProductoRef; }

    public Long getIdBodegaRef() { return idBodegaRef; }
    public void setIdBodegaRef(Long idBodegaRef) { this.idBodegaRef = idBodegaRef; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }

    public Integer getStockReservado() { return stockReservado; }
    public void setStockReservado(Integer stockReservado) { this.stockReservado = stockReservado; }

    public Integer getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(Integer stockDisponible) { this.stockDisponible = stockDisponible; }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    @Override
    public String toString() {
        return "ExistenciaResponseDTO{" +
                "idExistencia=" + idExistencia +
                ", idProductoRef=" + idProductoRef +
                ", idBodegaRef=" + idBodegaRef +
                ", stockActual=" + stockActual +
                ", stockReservado=" + stockReservado +
                ", stockDisponible=" + stockDisponible +
                ", stockMinimo=" + stockMinimo +
                '}';
    }
}
