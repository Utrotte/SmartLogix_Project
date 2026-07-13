package cl.smartlogix.envios.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seguimiento_envio")
public class SeguimientoEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSeguimientoEnvio;

    @ManyToOne
    @JoinColumn(name = "id_envio", nullable = false)
    private Envio envio;

    @Column(nullable = false)
    private LocalDateTime fechaEvento = LocalDateTime.now();

    @Column(nullable = false, length = 80)
    private String estadoEvento;

    @Column(length = 250)
    private String descripcion;

    public SeguimientoEnvio() {
    }

    public Long getIdSeguimientoEnvio() {
        return idSeguimientoEnvio;
    }

    public void setIdSeguimientoEnvio(Long idSeguimientoEnvio) {
        this.idSeguimientoEnvio = idSeguimientoEnvio;
    }

    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }

    public LocalDateTime getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDateTime fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getEstadoEvento() {
        return estadoEvento;
    }

    public void setEstadoEvento(String estadoEvento) {
        this.estadoEvento = estadoEvento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}