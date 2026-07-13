package com.smartlogix.clientes.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que almacena una dirección asociada a un cliente.
 */
@Entity
@Table(name = "direccion_cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Long idDireccion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "tipo_direccion", length = 50)
    private String tipoDireccion;

    @Column(length = 150)
    private String calle;

    @Column(length = 20)
    private String numero;

    @Column(length = 100)
    private String comuna;

    @Column(length = 100)
    private String ciudad;

    @Column(length = 100)
    private String region;

    @Column(name = "codigo_postal", length = 20)
    private String codigoPostal;

    @Column(name = "es_principal")
    @Builder.Default
    private Boolean esPrincipal = false;
}
