package com.smartlogix.usuarios.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/** Rol de seguridad del sistema (ADMIN, OPERADOR, CONSULTA, etc.). */
@Entity
@Table(name = "rol")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Column(nullable = false, unique = true)
    private String nombre;

    private String descripcion;

    @OneToMany(mappedBy = "rol")
    @Builder.Default
    private List<UsuarioRol> usuariosAsignados = new ArrayList<>();
}
