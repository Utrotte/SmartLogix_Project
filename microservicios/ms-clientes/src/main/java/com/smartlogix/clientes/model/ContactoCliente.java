package com.smartlogix.clientes.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un medio de contacto adicional de un cliente.
 */
@Entity
@Table(name = "contacto_cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contacto")
    private Long idContacto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "tipo_contacto", nullable = false, length = 50)
    private String tipoContacto;

    @Column(nullable = false, length = 150)
    private String valor;

    @Column(name = "es_principal")
    @Builder.Default
    private Boolean esPrincipal = false;
}
