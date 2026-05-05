package com.example.ms_pedidos_smartlogix.dto;

import java.math.BigDecimal;

public class DetallePedidoResponseDTO {
    private Long idDetalle;
    private Long idPedido;
    private Long idProductoRef;
    private String codigoSkuRef;
    private String nombreProductoSnapshot;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private String estadoDetalle;

    public DetallePedidoResponseDTO() {
    }

    public DetallePedidoResponseDTO(Long idDetalle, Long idPedido, Long idProductoRef, String codigoSkuRef,
                                     String nombreProductoSnapshot, Integer cantidad, BigDecimal precioUnitario,
                                     BigDecimal subtotal, String estadoDetalle) {
        this.idDetalle = idDetalle;
        this.idPedido = idPedido;
        this.idProductoRef = idProductoRef;
        this.codigoSkuRef = codigoSkuRef;
        this.nombreProductoSnapshot = nombreProductoSnapshot;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.estadoDetalle = estadoDetalle;
    }

    public Long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Long getIdProductoRef() {
        return idProductoRef;
    }

    public void setIdProductoRef(Long idProductoRef) {
        this.idProductoRef = idProductoRef;
    }

    public String getCodigoSkuRef() {
        return codigoSkuRef;
    }

    public void setCodigoSkuRef(String codigoSkuRef) {
        this.codigoSkuRef = codigoSkuRef;
    }

    public String getNombreProductoSnapshot() {
        return nombreProductoSnapshot;
    }

    public void setNombreProductoSnapshot(String nombreProductoSnapshot) {
        this.nombreProductoSnapshot = nombreProductoSnapshot;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getEstadoDetalle() {
        return estadoDetalle;
    }

    public void setEstadoDetalle(String estadoDetalle) {
        this.estadoDetalle = estadoDetalle;
    }
}
