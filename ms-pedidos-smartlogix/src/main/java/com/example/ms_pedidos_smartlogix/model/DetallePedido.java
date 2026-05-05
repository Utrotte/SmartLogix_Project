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

@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @Column(name = "id_producto_ref")
    private Long idProductoRef;

    @Column(name = "codigo_sku_ref", length = 80)
    private String codigoSkuRef;

    @Column(name = "nombre_producto_snapshot", length = 150)
    private String nombreProductoSnapshot;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "estado_detalle", length = 40)
    private String estadoDetalle;

    public DetallePedido() {
    }

    public DetallePedido(Pedido pedido, Long idProductoRef, String codigoSkuRef, 
                         String nombreProductoSnapshot, Integer cantidad, BigDecimal precioUnitario,
                         BigDecimal subtotal, String estadoDetalle) {
        this.pedido = pedido;
        this.idProductoRef = idProductoRef;
        this.codigoSkuRef = codigoSkuRef;
        this.nombreProductoSnapshot = nombreProductoSnapshot;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.estadoDetalle = estadoDetalle;
    }

    @PrePersist
    protected void onCreate() {
        if (estadoDetalle == null) {
            estadoDetalle = "PENDIENTE";
        }
        if (subtotal == null && cantidad != null && precioUnitario != null) {
            subtotal = precioUnitario.multiply(new BigDecimal(cantidad));
        }
    }

    public Long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
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
