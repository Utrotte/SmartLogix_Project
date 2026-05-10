package cl.programadormaldito.ms_envios.model;

import java.time.LocalDateTime;

import cl.programadormaldito.ms_envios.util.GeneradorStringUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

// tabla técnica del patrón Outbox: garantiza que los eventos lleguen a otros microservicios
// aunque el broker de mensajes esté caído al momento del cambio de estado
@Entity
@Table(name = "evento_outbox_envio")
public class EventoOutboxEnvio {

    @Id
    @Column(name = "eo_id", length = 40, nullable = false)
    private String id;

    // id del envío que generó este evento
    @Column(name = "eo_id_envio_ref", length = 40, nullable = false)
    private String idEnvioRef;

    // ej: "ENVIO_CREADO", "TRANSPORTISTA_ASIGNADO", "ESTADO_ACTUALIZADO_EN_TRANSITO"
    @Column(name = "eo_tipo_evento", length = 80, nullable = false)
    private String tipoEvento;

    // cuerpo del mensaje en formato JSON que se enviará al siguiente servicio
    @Lob
    @Column(name = "eo_payload", columnDefinition = "TEXT")
    private String payload;

    // PENDIENTE = aún no fue procesado, PROCESADO = ya se publicó, ERROR = falló
    @Column(name = "eo_estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "eo_fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    // permite rastrear este evento en los logs de auditoría del sistema
    @Column(name = "eo_correlation_id", length = 40)
    private String correlationId;

    public EventoOutboxEnvio() {
        this.id = GeneradorStringUtil.generarID();
        this.idEnvioRef = "";
        this.tipoEvento = "";
        this.payload = "";
        this.estado = "PENDIENTE";
        this.fechaCreacion = LocalDateTime.now();
        this.correlationId = GeneradorStringUtil.generarID(); // ID único para trazabilidad
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getIdEnvioRef() { return idEnvioRef; }
    public void setIdEnvioRef(String idEnvioRef) { this.idEnvioRef = idEnvioRef; }
    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
}
