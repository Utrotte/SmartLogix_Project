package cl.smartlogix.transportistas.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "disponibilidad_transportista")
public class DisponibilidadTransportista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDisponibilidadTransportista;

    @ManyToOne
    @JoinColumn(name = "id_transportista", nullable = false)
    private Transportista transportista;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 50)
    private String zonaDisponible;

    @Column(nullable = false)
    private Integer cuposDisponibles;

    @Column(nullable = false)
    private Boolean disponible = true;

    public DisponibilidadTransportista() {
    }

    public Long getIdDisponibilidadTransportista() {
        return idDisponibilidadTransportista;
    }

    public void setIdDisponibilidadTransportista(Long idDisponibilidadTransportista) {
        this.idDisponibilidadTransportista = idDisponibilidadTransportista;
    }

    public Transportista getTransportista() {
        return transportista;
    }

    public void setTransportista(Transportista transportista) {
        this.transportista = transportista;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getZonaDisponible() {
        return zonaDisponible;
    }

    public void setZonaDisponible(String zonaDisponible) {
        this.zonaDisponible = zonaDisponible;
    }

    public Integer getCuposDisponibles() {
        return cuposDisponibles;
    }

    public void setCuposDisponibles(Integer cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}