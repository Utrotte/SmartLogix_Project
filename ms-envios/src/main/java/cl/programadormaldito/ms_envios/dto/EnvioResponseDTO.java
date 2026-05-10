package cl.programadormaldito.ms_envios.dto;

import java.time.LocalDate;

// respuesta que devuelve el servicio con los datos clave del envío
public class EnvioResponseDTO {

    private String id;
    private String idPedidoRef;
    private String codigoEnvio;
    private String estado;
    private LocalDate fechaProgramada;
    private LocalDate fechaEstimada;
    private Double costoTotal;
    private String idTransportista;
    private String nombreTransportista; // incluido para no tener que hacer otra consulta

    public EnvioResponseDTO() {
        this.id = "";
        this.idPedidoRef = "";
        this.codigoEnvio = "";
        this.estado = "";
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
    public String getIdTransportista() { return idTransportista; }
    public void setIdTransportista(String idTransportista) { this.idTransportista = idTransportista; }
    public String getNombreTransportista() { return nombreTransportista; }
    public void setNombreTransportista(String nombreTransportista) { this.nombreTransportista = nombreTransportista; }
}
