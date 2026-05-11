package cl.programadormaldito.ms_envios.model;

import java.time.LocalDate;
import java.util.List;

import cl.programadormaldito.ms_envios.util.GeneradorStringUtil;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// entidad principal — orquesta todo el proceso de despacho
@Entity
@Table(name = "envio")
public class Envio {

    @Id
    @Column(name = "e_id", length = 40, nullable = false)
    private String id;

    // referencia lógica al pedido que originó este envío (no es FK física)
    @Column(name = "e_id_pedido_ref", length = 40, nullable = false)
    private String idPedidoRef;

    // código legible para el cliente, ej: ENV-A3KX9ZBQ12-20250504
    @Column(name = "e_codigo_envio", length = 30, unique = true, nullable = false)
    private String codigoEnvio;

    // ciclo de vida: PENDIENTE → ASIGNADO → EN_TRANSITO → ENTREGADO
    @Column(name = "e_estado", length = 30, nullable = false)
    private String estado;

    @Column(name = "e_fecha_programada")
    private LocalDate fechaProgramada;

    @Column(name = "e_fecha_estimada")
    private LocalDate fechaEstimada;

    @Column(name = "e_costo_total")
    private Double costoTotal;

    // puede ser null si aún no se asignó transportista (estado PENDIENTE)
    @ManyToOne
    @JoinColumn(name = "e_id_transportista")
    private Transportista transportista;

    // historial de eventos — la FK está en la tabla seguimiento_envio
    @OneToMany(mappedBy = "envio", cascade = CascadeType.ALL)
    private List<SeguimientoEnvio> seguimientos;

    // las FK de estas relaciones 1:1 viven en cada tabla hija
    @OneToOne(mappedBy = "envio", cascade = CascadeType.ALL)
    private GuiaDespacho guiaDespacho;

    @OneToOne(mappedBy = "envio", cascade = CascadeType.ALL)
    private DireccionEnvio direccionEnvio;

    @OneToOne(mappedBy = "envio", cascade = CascadeType.ALL)
    private PaqueteEnvio paqueteEnvio;

    @OneToOne(mappedBy = "envio", cascade = CascadeType.ALL)
    private RutaEntrega rutaEntrega;

    public Envio() {
        this.id = GeneradorStringUtil.generarID();
        this.idPedidoRef = "";
        this.codigoEnvio = GeneradorStringUtil.generarCodigoEnvio();
        this.estado = "PENDIENTE_ASIGNACION"; // alineado con estados del BFF y del frontend
        this.costoTotal = 0.0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getIdPedidoRef() { return idPedidoRef; }
    public void setIdPedidoRef(String idPedidoRef) { this.idPedidoRef = idPedidoRef; }
    public String getCodigoEnvio() { return codigoEnvio; }
    public void setCodigoEnvio(String codigoEnvio) { this.codigoEnvio = codigoEnvio; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDate getFechaProgramada() { return fechaProgramada; }
    public void setFechaProgramada(LocalDate fechaProgramada) { this.fechaProgramada = fechaProgramada; }
    public LocalDate getFechaEstimada() { return fechaEstimada; }
    public void setFechaEstimada(LocalDate fechaEstimada) { this.fechaEstimada = fechaEstimada; }
    public Double getCostoTotal() { return costoTotal; }
    public void setCostoTotal(Double costoTotal) { this.costoTotal = costoTotal; }
    public Transportista getTransportista() { return transportista; }
    public void setTransportista(Transportista transportista) { this.transportista = transportista; }
    public List<SeguimientoEnvio> getSeguimientos() { return seguimientos; }
    public void setSeguimientos(List<SeguimientoEnvio> seguimientos) { this.seguimientos = seguimientos; }
    public GuiaDespacho getGuiaDespacho() { return guiaDespacho; }
    public void setGuiaDespacho(GuiaDespacho guiaDespacho) { this.guiaDespacho = guiaDespacho; }
    public DireccionEnvio getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(DireccionEnvio direccionEnvio) { this.direccionEnvio = direccionEnvio; }
    public PaqueteEnvio getPaqueteEnvio() { return paqueteEnvio; }
    public void setPaqueteEnvio(PaqueteEnvio paqueteEnvio) { this.paqueteEnvio = paqueteEnvio; }
    public RutaEntrega getRutaEntrega() { return rutaEntrega; }
    public void setRutaEntrega(RutaEntrega rutaEntrega) { this.rutaEntrega = rutaEntrega; }
}
