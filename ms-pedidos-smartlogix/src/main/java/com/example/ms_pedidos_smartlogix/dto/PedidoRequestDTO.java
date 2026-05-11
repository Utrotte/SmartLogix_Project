package com.example.ms_pedidos_smartlogix.dto;

import java.math.BigDecimal;

import java.util.List;

public class PedidoRequestDTO {
    private ClienteRequestDTO cliente;
    private String codigoPedido;
    private String canalOrigen;
    private BigDecimal totalBruto;
    private BigDecimal descuentoTotal;
    private BigDecimal totalNeto;
    private String observacion;
    private List<DetallePedidoRequestDTO> detalles;
    private DireccionEntregaRequestDTO direccionEntrega;

    public PedidoRequestDTO() {
    }

    public PedidoRequestDTO(ClienteRequestDTO cliente, String codigoPedido, String canalOrigen, 
                            BigDecimal totalBruto, BigDecimal descuentoTotal, BigDecimal totalNeto, String observacion,
                            List<DetallePedidoRequestDTO> detalles, DireccionEntregaRequestDTO direccionEntrega) {
        this.cliente = cliente;
        this.codigoPedido = codigoPedido;
        this.canalOrigen = canalOrigen;
        this.totalBruto = totalBruto;
        this.descuentoTotal = descuentoTotal;
        this.totalNeto = totalNeto;
        this.observacion = observacion;
        this.detalles = detalles;
        this.direccionEntrega = direccionEntrega;
    }

    public ClienteRequestDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteRequestDTO cliente) {
        this.cliente = cliente;
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

    public List<DetallePedidoRequestDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedidoRequestDTO> detalles) {
        this.detalles = detalles;
    }

    public DireccionEntregaRequestDTO getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(DireccionEntregaRequestDTO direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }
}
