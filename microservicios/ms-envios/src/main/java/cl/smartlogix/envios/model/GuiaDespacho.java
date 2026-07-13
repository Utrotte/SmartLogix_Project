package cl.smartlogix.envios.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "guia_despacho")
public class GuiaDespacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGuiaDespacho;

    @OneToOne
    @JoinColumn(name = "id_envio", nullable = false)
    private Envio envio;

    @Column(nullable = false, length = 100)
    private String numeroGuia;

    @Column(nullable = false)
    private LocalDateTime fechaEmision = LocalDateTime.now();

    @Column(length = 250)
    private String documentoUrl;

    public GuiaDespacho() {
    }

    public Long getIdGuiaDespacho() {
        return idGuiaDespacho;
    }

    public void setIdGuiaDespacho(Long idGuiaDespacho) {
        this.idGuiaDespacho = idGuiaDespacho;
    }

    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getDocumentoUrl() {
        return documentoUrl;
    }

    public void setDocumentoUrl(String documentoUrl) {
        this.documentoUrl = documentoUrl;
    }
}