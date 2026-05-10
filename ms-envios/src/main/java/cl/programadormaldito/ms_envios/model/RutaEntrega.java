package cl.programadormaldito.ms_envios.model;

import cl.programadormaldito.ms_envios.util.GeneradorStringUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// información de la ruta que tomará el paquete para llegar al destino
@Entity
@Table(name = "ruta_entrega")
public class RutaEntrega {

    @Id
    @Column(name = "re_id", length = 40, nullable = false)
    private String id;

    @Column(name = "re_distancia_km")
    private Double distanciaKm;

    // NORMAL, URGENTE, PROGRAMADA
    @Column(name = "re_prioridad", length = 20)
    private String prioridad;

    @Column(name = "re_observacion", length = 255)
    private String observacion;

    // FK hacia el envío — la relación 1:1 se gestiona desde este lado
    @OneToOne
    @JoinColumn(name = "re_id_envio", nullable = false)
    private Envio envio;

    public RutaEntrega() {
        this.id = GeneradorStringUtil.generarID();
        this.distanciaKm = 0.0;
        this.prioridad = "NORMAL"; // prioridad por defecto
        this.observacion = "";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(Double distanciaKm) { this.distanciaKm = distanciaKm; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public Envio getEnvio() { return envio; }
    public void setEnvio(Envio envio) { this.envio = envio; }
}
