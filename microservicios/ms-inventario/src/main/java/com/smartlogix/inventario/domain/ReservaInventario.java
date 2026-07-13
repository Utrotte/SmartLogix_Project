package com.smartlogix.inventario.domain;

import com.smartlogix.inventario.domain.enums.EstadoReserva;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas_inventario")
public class ReservaInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idProducto;

    private Long idBodega;

    private Long idPedido;

    private Integer cantidadReservada;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estadoReserva;

    private LocalDateTime fechaReserva = LocalDateTime.now();

    public ReservaInventario() {}

    public ReservaInventario(Long idProducto, Long idBodega, Long idPedido, Integer cantidadReservada, EstadoReserva estadoReserva) {
        this.idProducto = idProducto;
        this.idBodega = idBodega;
        this.idPedido = idPedido;
        this.cantidadReservada = cantidadReservada;
        this.estadoReserva = estadoReserva;
    }

    public Long getId() { return id; }
    public Long getIdProducto() { return idProducto; }
    public Long getIdBodega() { return idBodega; }
    public Long getIdPedido() { return idPedido; }
    public Integer getCantidadReservada() { return cantidadReservada; }
    public EstadoReserva getEstadoReserva() { return estadoReserva; }
    public void setEstadoReserva(EstadoReserva estadoReserva) { this.estadoReserva = estadoReserva; }
    public LocalDateTime getFechaReserva() { return fechaReserva; }
}
