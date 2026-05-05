package com.example.ms_pedidos_smartlogix.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long idPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "codigo_pedido", length = 80, unique = true)
    private String codigoPedido;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "estado_actual", length = 40)
    private String estadoActual;

    @Column(name = "canal_origen", length = 80)
    private String canalOrigen;

    @Column(name = "total_bruto", precision = 12, scale = 2)
    private BigDecimal totalBruto;

    @Column(name = "descuento_total", precision = 12, scale = 2)
    private BigDecimal descuentoTotal;

    @Column(name = "total_neto", precision = 12, scale = 2)
    private BigDecimal totalNeto;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    public Pedido() {
    }

    public Pedido(Cliente cliente, String codigoPedido, String canalOrigen, BigDecimal totalBruto,
                  BigDecimal descuentoTotal, BigDecimal totalNeto, String observacion) {
        this.cliente = cliente;
        this.codigoPedido = codigoPedido;
        this.canalOrigen = canalOrigen;
        this.totalBruto = totalBruto;
        this.descuentoTotal = descuentoTotal;
        this.totalNeto = totalNeto;
        this.observacion = observacion;
    }

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (estadoActual == null) {
            estadoActual = "CREADO";
        }
        if (totalBruto == null) {
            totalBruto = BigDecimal.ZERO;
        }
        if (descuentoTotal == null) {
            descuentoTotal = BigDecimal.ZERO;
        }
        if (totalNeto == null) {
            totalNeto = BigDecimal.ZERO;
        }
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
}
