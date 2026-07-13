package com.smartlogix.clientes.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad principal que representa un cliente registrado en el sistema.
 */
@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tipo_cliente", nullable = false)
    private TipoCliente tipoCliente;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "apellido_razon_social", length = 150)
    private String apellidoRazonSocial;

    @Column(unique = true, length = 30)
    private String documento;

    @Column(unique = true, length = 150)
    private String correo;

    @Column(length = 30)
    private String telefono;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String estado = "ACTIVO";

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DireccionCliente> direcciones = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ContactoCliente> contactos = new ArrayList<>();

    /**
     * Asigna la fecha de creación automáticamente al persistir el registro.
     */
    @PrePersist
    void alCrear() {
        fechaCreacion = LocalDateTime.now();
    }
}
