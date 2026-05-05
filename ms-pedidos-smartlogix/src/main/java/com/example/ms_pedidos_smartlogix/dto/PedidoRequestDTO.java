package com.example.ms_pedidos_smartlogix.dto;

import java.math.BigDecimal;

public class PedidoRequestDTO {
    private Long idCliente;
    private String codigoPedido;
    private String canalOrigen;
    private BigDecimal totalBruto;
    private BigDecimal descuentoTotal;
    private BigDecimal totalNeto;
    private String observacion;

    public PedidoRequestDTO() {
    }

    public PedidoRequestDTO(Long idCliente, String codigoPedido, String canalOrigen, 
                            BigDecimal totalBruto, BigDecimal descuentoTotal, BigDecimal totalNeto, String observacion) {
        this.idCliente = idCliente;
        this.codigoPedido = codigoPedido;
        this.canalOrigen = canalOrigen;
        this.totalBruto = totalBruto;
        this.descuentoTotal = descuentoTotal;
        this.totalNeto = totalNeto;
        this.observacion = observacion;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public String getCanalOrigen() {
        return canalOrigen;
    }

    public void setCanalOrigen(String canalOrigen) {
        this.canalOrigen = canalOrigen;
    }

    public BigDecimal getTotalBruto() {
        return totalBruto;
    }

    public void setTotalBruto(BigDecimal totalBruto) {
        this.totalBruto = totalBruto;
    }

    public BigDecimal getDescuentoTotal() {
        return descuentoTotal;
    }

    public void setDescuentoTotal(BigDecimal descuentoTotal) {
        this.descuentoTotal = descuentoTotal;
    }

    public BigDecimal getTotalNeto() {
        return totalNeto;
    }

    public void setTotalNeto(BigDecimal totalNeto) {
        this.totalNeto = totalNeto;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
