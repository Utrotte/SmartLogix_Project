package com.smartlogix.pedidos.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idPedido;

    private Long idProducto;

    private Long idBodega;

    private Integer cantidad;

    private BigDecimal precioUnitario;

    private BigDecimal subtotal;

}
