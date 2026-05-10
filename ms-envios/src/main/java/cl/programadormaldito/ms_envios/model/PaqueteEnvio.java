package cl.programadormaldito.ms_envios.model;

import cl.programadormaldito.ms_envios.util.GeneradorStringUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// dimensiones físicas del paquete — necesarias para el cálculo logístico
@Entity
@Table(name = "paquete_envio")
public class PaqueteEnvio {

    @Id
    @Column(name = "pe_id", length = 40, nullable = false)
    private String id;

    @Column(name = "pe_peso_kg")
    private Double pesoKg;

    @Column(name = "pe_alto_cm")
    private Double altoCm;

    @Column(name = "pe_ancho_cm")
    private Double anchoCm;

    @Column(name = "pe_largo_cm")
    private Double largoCm;

    @Column(name = "pe_descripcion_contenido", length = 255)
    private String descripcionContenido;

    // FK hacia el envío — la relación 1:1 se gestiona desde este lado
    @OneToOne
    @JoinColumn(name = "pe_id_envio", nullable = false)
    private Envio envio;

    public PaqueteEnvio() {
        this.id = GeneradorStringUtil.generarID();
        this.pesoKg = 0.0;
        this.altoCm = 0.0;
        this.anchoCm = 0.0;
        this.largoCm = 0.0;
        this.descripcionContenido = "";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Double getPesoKg() { return pesoKg; }
    public void setPesoKg(Double pesoKg) { this.pesoKg = pesoKg; }
    public Double getAltoCm() { return altoCm; }
    public void setAltoCm(Double altoCm) { this.altoCm = altoCm; }
    public Double getAnchoCm() { return anchoCm; }
    public void setAnchoCm(Double anchoCm) { this.anchoCm = anchoCm; }
    public Double getLargoCm() { return largoCm; }
    public void setLargoCm(Double largoCm) { this.largoCm = largoCm; }
    public String getDescripcionContenido() { return descripcionContenido; }
    public void setDescripcionContenido(String descripcionContenido) { this.descripcionContenido = descripcionContenido; }
    public Envio getEnvio() { return envio; }
    public void setEnvio(Envio envio) { this.envio = envio; }
}
