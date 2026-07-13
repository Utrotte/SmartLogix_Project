package com.smartlogix.usuarios.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un usuario interno del sistema SmartLogix (tabla usuario_sistema).
 * No confundir con el cliente final: estos usuarios operan la plataforma.
 */
@Entity
@Table(name = "usuario_sistema")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String correo;

    /** Contraseña almacenada con hash BCrypt, nunca en texto plano. */
    @Column(name = "password_hash", nullable = false)
    private String hashContrasena;

    @Column(nullable = false)
    @Builder.Default
    private String estado = "ACTIVO";

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UsuarioRol> rolesAsignados = new ArrayList<>();

    @PrePersist
    void alCrear() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    void alActualizar() {
        fechaActualizacion = LocalDateTime.now();
    }
}
