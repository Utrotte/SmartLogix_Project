package com.smartlogix.inventario_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reserva_inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva; // Clave primaria

    @NotNull(message = "El ID del pedido es obligatorio")
    @Positive(message = "El ID del pedido debe ser positivo")
    @Column(name = "id_pedido_ref", nullable = false)
    private Long idPedidoRef; // Referencia a pedido externo

    @NotBlank(message = "El código del pedido es obligatorio")
    @Column(name = "codigo_pedido_ref", nullable = false)
    private String codigoPedidoRef; // Código del pedido

    @Column(name = "estado_reserva")
    @Builder.Default
    private String estadoReserva = "CREADA"; // CREADA, CONFIRMADA, CANCELADA

    @Column(name = "fecha_reserva", nullable = false, updatable = false)
    private LocalDateTime fechaReserva; // Fecha de creación

    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion; // Fecha de vencimiento de la reserva

    // Asigna la fecha automáticamente al crear
    @PrePersist
    protected void onCreate() {
        if (fechaReserva == null) {
            fechaReserva = LocalDateTime.now();
        }
    }
}
