package com.example.ms_pedidos_smartlogix.dto;

import java.math.BigDecimal;

public class PagoPedidoRequestDTO {
    private Long idPedido;
    private String metodoPago;
    private String estadoPago;
    private BigDecimal monto;
    private String codigoTransaccion;

    public PagoPedidoRequestDTO() {
    }

    public PagoPedidoRequestDTO(Long idPedido, String metodoPago, String estadoPago,
                                 BigDecimal monto, String codigoTransaccion) {
        this.idPedido = idPedido;
        this.metodoPago = metodoPago;
        this.estadoPago = estadoPago;
        this.monto = monto;
        this.codigoTransaccion = codigoTransaccion;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getCodigoTransaccion() {
        return codigoTransaccion;
    }

    public void setCodigoTransaccion(String codigoTransaccion) {
        this.codigoTransaccion = codigoTransaccion;
    }
}
