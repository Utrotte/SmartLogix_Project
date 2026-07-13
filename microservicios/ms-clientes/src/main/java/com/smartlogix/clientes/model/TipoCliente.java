package com.smartlogix.clientes.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un tipo de cliente (por ejemplo PERSONA o EMPRESA).
 */
@Entity
@Table(name = "tipo_cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_cliente")
    private Long idTipoCliente;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @OneToMany(mappedBy = "tipoCliente")
    @Builder.Default
    private List<Cliente> clientes = new ArrayList<>();
}
