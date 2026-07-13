package cl.smartlogix.transportistas.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tarifa_ruta")
public class TarifaRuta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTarifaRuta;

    @ManyToOne
    @JoinColumn(name = "id_transportista", nullable = false)
    private Transportista transportista;

    @ManyToOne
    @JoinColumn(name = "id_ruta", nullable = false)
    private Ruta ruta;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal costoEstimado;

    @Column(nullable = false)
    private Integer tiempoEstimadoHoras;

    @Column(nullable = false)
    private Boolean activa = true;

    public TarifaRuta() {
    }

    public Long getIdTarifaRuta() {
        return idTarifaRuta;
    }

    public void setIdTarifaRuta(Long idTarifaRuta) {
        this.idTarifaRuta = idTarifaRuta;
    }

    public Transportista getTransportista() {
        return transportista;
    }

    public void setTransportista(Transportista transportista) {
        this.transportista = transportista;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public BigDecimal getCostoEstimado() {
        return costoEstimado;
    }

    public void setCostoEstimado(BigDecimal costoEstimado) {
        this.costoEstimado = costoEstimado;
    }

    public Integer getTiempoEstimadoHoras() {
        return tiempoEstimadoHoras;
    }

    public void setTiempoEstimadoHoras(Integer tiempoEstimadoHoras) {
        this.tiempoEstimadoHoras = tiempoEstimadoHoras;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
}