package cl.programadormaldito.ms_envios.model;

import java.time.LocalDate;

import cl.programadormaldito.ms_envios.util.GeneradorStringUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// documento legal asociado al despacho del envío
@Entity
@Table(name = "guia_despacho")
public class GuiaDespacho {

    @Id
    @Column(name = "gd_id", length = 40, nullable = false)
    private String id;

    // número único del documento, ej: "GD-2025-000123"
    @Column(name = "gd_numero_guia", length = 50, unique = true, nullable = false)
    private String numeroGuia;

    @Column(name = "gd_fecha_emision")
    private LocalDate fechaEmision;

    // URL donde está almacenado el documento digitalizado
    @Column(name = "gd_documento_url", length = 255)
    private String documentoUrl;

    // FK hacia el envío — la relación 1:1 se gestiona desde este lado
    @OneToOne
    @JoinColumn(name = "gd_id_envio", nullable = false)
    private Envio envio;

    public GuiaDespacho() {
        this.id = GeneradorStringUtil.generarID();
        this.numeroGuia = "";
        this.documentoUrl = "";
        this.fechaEmision = LocalDate.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNumeroGuia() { return numeroGuia; }
    public void setNumeroGuia(String numeroGuia) { this.numeroGuia = numeroGuia; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }
    public String getDocumentoUrl() { return documentoUrl; }
    public void setDocumentoUrl(String documentoUrl) { this.documentoUrl = documentoUrl; }
    public Envio getEnvio() { return envio; }
    public void setEnvio(Envio envio) { this.envio = envio; }
}
