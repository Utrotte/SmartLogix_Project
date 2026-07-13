package com.smartlogix.usuarios.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Registra sesiones activas con token JWT.
 * Permite invalidar sesiones al cerrar sesión (logout).
 */
@Entity
@Table(name = "sesion_usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SesionUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
    private Long idSesion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioSistema usuario;

    @Column(name = "token_jwt", nullable = false, unique = true, length = 512)
    private String tokenJwt;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activa = true;

    @Column(name = "ip_origen")
    private String ipOrigen;

    @Column(name = "user_agent", length = 500)
    private String agenteUsuario;
}
