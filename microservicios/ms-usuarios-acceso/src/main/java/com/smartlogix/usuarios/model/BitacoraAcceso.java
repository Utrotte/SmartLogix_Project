package com.smartlogix.usuarios.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Bitácora de eventos de acceso: logins exitosos, fallidos, logout, etc.
 * Útil para auditoría y seguridad.
 */
@Entity
@Table(name = "bitacora_acceso")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BitacoraAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bitacora")
    private Long idBitacora;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "correo_intento")
    private String correoIntento;

    @Column(name = "tipo_evento", nullable = false)
    private String tipoEvento;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "ip_origen")
    private String ipOrigen;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDateTime fechaEvento;

    @Column(nullable = false)
    private Boolean exitoso;

    @PrePersist
    void alCrear() {
        if (fechaEvento == null) {
            fechaEvento = LocalDateTime.now();
        }
    }
}
