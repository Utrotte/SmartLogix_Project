package cl.programadormaldito.ms_envios.model;

import java.time.LocalDateTime;

import cl.programadormaldito.ms_envios.util.GeneradorStringUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// registra cada evento del trayecto del paquete (ej: "llegó a bodega", "en ruta")
@Entity
@Table(name = "seguimiento_envio")
public class SeguimientoEnvio {

    @Id
    @Column(name = "se_id", length = 40, nullable = false)
    private String id;

    // se captura automáticamente al crear el registro
    @Column(name = "se_fecha_evento", nullable = false)
    private LocalDateTime fechaEvento;

    // descripción del evento, ej: "Llegó a centro de distribución Pudahuel"
    @Column(name = "se_descripcion", length = 255, nullable = false)
    private String descripcion;

    @Column(name = "se_ubicacion", length = 150)
    private String ubicacion;

    // FK hacia el envío al que pertenece este evento
    @ManyToOne
    @JoinColumn(name = "se_id_envio", nullable = false)
    private Envio envio;

    public SeguimientoEnvio() {
        this.id = GeneradorStringUtil.generarID();
        this.fechaEvento = LocalDateTime.now(); // hora exacta del evento
        this.descripcion = "";
        this.ubicacion = "";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public LocalDateTime getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(LocalDateTime fechaEvento) { this.fechaEvento = fechaEvento; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public Envio getEnvio() { return envio; }
    public void setEnvio(Envio envio) { this.envio = envio; }
}
