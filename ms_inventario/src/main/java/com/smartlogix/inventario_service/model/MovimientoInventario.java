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
@Table(name = "movimiento_inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long idMovimiento; // Clave primaria

    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto; // Relación con producto

    @NotNull(message = "La bodega es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bodega", nullable = false)
    private Bodega bodega; // Relación con bodega

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva")
    private ReservaInventario reserva; // Reserva asociada (si aplica)

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Column(name = "tipo_movimiento", nullable = false)
    private String tipoMovimiento; // ENTRADA, SALIDA, AJUSTE, etc.

    @Positive(message = "La cantidad debe ser mayor a 0")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "stock_resultante")
    private Integer stockResultante; // Stock después del movimiento

    @Column(name = "referencia")
    private String referencia; // Referencia a comprobante, guía, etc.

    @Column(name = "fecha_movimiento", nullable = false, updatable = false)
    private LocalDateTime fechaMovimiento; // Timestamp del movimiento

    @Column(name = "observacion")
    private String observacion;

    @PrePersist
    protected void onCreate() {
        if (fechaMovimiento == null) {
            fechaMovimiento = LocalDateTime.now();
        }
    }
}
