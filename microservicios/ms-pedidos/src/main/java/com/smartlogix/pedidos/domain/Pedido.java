package com.smartlogix.pedidos.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idCliente;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estadoPedido;

    private BigDecimal total;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    private boolean activo;

}
