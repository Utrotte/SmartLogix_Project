package cl.programadormaldito.ms_envios.integration.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoResponseDTO {

    private Long idPedido;
    private String codigoPedido;
    private Long idClienteRef;
    private String estadoActual;
    private BigDecimal totalNeto;
    private BigDecimal descuentoTotal;
    private String observacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimaModificacion;

    // Constructor
    public PedidoResponseDTO() {}

    // Getters y Setters
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public String getCodigoPedido() { return codigoPedido; }
    public void setCodigoPedido(String codigoPedido) { this.codigoPedido = codigoPedido; }

    public Long getIdClienteRef() { return idClienteRef; }
    public void setIdClienteRef(Long idClienteRef) { this.idClienteRef = idClienteRef; }

    public String getEstadoActual() { return estadoActual; }
    public void setEstadoActual(String estadoActual) { this.estadoActual = estadoActual; }

    public BigDecimal getTotalNeto() { return totalNeto; }
    public void setTotalNeto(BigDecimal totalNeto) { this.totalNeto = totalNeto; }

    public BigDecimal getDescuentoTotal() { return descuentoTotal; }
    public void setDescuentoTotal(BigDecimal descuentoTotal) { this.descuentoTotal = descuentoTotal; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaUltimaModificacion() { return fechaUltimaModificacion; }
    public void setFechaUltimaModificacion(LocalDateTime fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

    @Override
    public String toString() {
        return "PedidoResponseDTO{" +
                "idPedido=" + idPedido +
                ", codigoPedido='" + codigoPedido + '\'' +
                ", idClienteRef=" + idClienteRef +
                ", estadoActual='" + estadoActual + '\'' +
                ", totalNeto=" + totalNeto +
                ", descuentoTotal=" + descuentoTotal +
                ", observacion='" + observacion + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaUltimaModificacion=" + fechaUltimaModificacion +
                '}';
    }
}
