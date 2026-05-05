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
import java.time.LocalDateTime;

@Entity
@Table(name = "estado_pedido_historial")
public class EstadoPedidoHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_historial")
    private Long idEstadoHistorial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @Column(name = "estado", length = 40)
    private String estado;

    @Column(name = "fecha_estado")
    private LocalDateTime fechaEstado;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @Column(name = "usuario_responsable", length = 120)
    private String usuarioResponsable;

    public EstadoPedidoHistorial() {
    }

    public EstadoPedidoHistorial(Pedido pedido, String estado, String observacion, String usuarioResponsable) {
        this.pedido = pedido;
        this.estado = estado;
        this.observacion = observacion;
        this.usuarioResponsable = usuarioResponsable;
    }

    @PrePersist
    protected void onCreate() {
        if (fechaEstado == null) {
            fechaEstado = LocalDateTime.now();
        }
    }

    public Long getIdEstadoHistorial() {
        return idEstadoHistorial;
    }

    public void setIdEstadoHistorial(Long idEstadoHistorial) {
        this.idEstadoHistorial = idEstadoHistorial;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaEstado() {
        return fechaEstado;
    }

    public void setFechaEstado(LocalDateTime fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(String usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }
}
