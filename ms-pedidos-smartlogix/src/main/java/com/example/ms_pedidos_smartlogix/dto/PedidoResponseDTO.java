package com.example.ms_pedidos_smartlogix.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDTO {
    private Long idPedido;
    private Long idCliente;
    private String nombreCliente;
    private String codigoPedido;
    private LocalDateTime fechaCreacion;
    private String estadoActual;
    private String canalOrigen;
    private BigDecimal totalBruto;
    private BigDecimal descuentoTotal;
    private BigDecimal totalNeto;
    private String observacion;
    private List<DetallePedidoResponseDTO> detalles;
    private DireccionEntregaResponseDTO direccionEntrega;

    public PedidoResponseDTO() {
    }

    public PedidoResponseDTO(Long idPedido, Long idCliente, String nombreCliente, String codigoPedido,
                             LocalDateTime fechaCreacion, String estadoActual, String canalOrigen,
                             BigDecimal totalBruto, BigDecimal descuentoTotal, BigDecimal totalNeto, String observacion,
                             List<DetallePedidoResponseDTO> detalles, DireccionEntregaResponseDTO direccionEntrega) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.codigoPedido = codigoPedido;
        this.fechaCreacion = fechaCreacion;
        this.estadoActual = estadoActual;
        this.canalOrigen = canalOrigen;
        this.totalBruto = totalBruto;
        this.descuentoTotal = descuentoTotal;
        this.totalNeto = totalNeto;
        this.observacion = observacion;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
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

    public List<DetallePedidoResponseDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedidoResponseDTO> detalles) {
        this.detalles = detalles;
    }

    public DireccionEntregaResponseDTO getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(DireccionEntregaResponseDTO direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }
}
