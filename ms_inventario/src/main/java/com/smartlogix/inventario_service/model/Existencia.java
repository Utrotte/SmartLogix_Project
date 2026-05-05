package com.smartlogix.inventario_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "existencia", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_producto", "id_bodega"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Existencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_existencia")
    private Long idExistencia; // Clave primaria

    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto; // Relación con producto

    @NotNull(message = "La bodega es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bodega", nullable = false)
    private Bodega bodega; // Relación con bodega

    @PositiveOrZero(message = "El stock actual no puede ser negativo")
    @Column(name = "stock_actual")
    @Builder.Default
    private Integer stockActual = 0; // Cantidad total en bodega

    @PositiveOrZero(message = "El stock reservado no puede ser negativo")
    @Column(name = "stock_reservado")
    @Builder.Default
    private Integer stockReservado = 0; // Cantidad comprometida

    @PositiveOrZero(message = "El stock disponible no puede ser negativo")
    @Column(name = "stock_disponible")
    @Builder.Default
    private Integer stockDisponible = 0; // stockActual - stockReservado

    @PositiveOrZero(message = "El stock mínimo no puede ser negativo")
    @Column(name = "stock_minimo")
    @Builder.Default
    private Integer stockMinimo = 0; // Límite para alertas

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion; // Última modificación

    @PrePersist
    protected void onCreate() {
        fechaActualizacion = LocalDateTime.now();
        recalcularStockDisponible();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Recalcula el stock disponible según la fórmula: stockActual - stockReservado
    public void recalcularStockDisponible() {
        this.stockDisponible = this.stockActual - this.stockReservado;
        if (this.stockDisponible < 0) {
            this.stockDisponible = 0;
        }
    }
}
