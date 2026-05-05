package com.smartlogix.inventario_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reserva_inventario_detalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaInventarioDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva_detalle")
    private Long idReservaDetalle;

    @NotNull(message = "La reserva es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false)
    private ReservaInventario reserva;

    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @NotNull(message = "La bodega es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bodega", nullable = false)
    private Bodega bodega;

    @Positive(message = "La cantidad reservada debe ser mayor a 0")
    @Column(name = "cantidad_reservada", nullable = false)
    private Integer cantidadReservada;

    @Column(name = "estado_detalle")
    @Builder.Default
    private String estadoDetalle = "RESERVADO";
}
