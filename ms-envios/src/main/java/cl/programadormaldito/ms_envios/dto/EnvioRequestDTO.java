package cl.programadormaldito.ms_envios.dto;

import java.time.LocalDate;

// datos que el cliente envía al crear un nuevo envío
// agrupa en una sola petición: info del envío, dirección de destino y paquete
public class EnvioRequestDTO {

    private String idPedidoRef;     // id del pedido que origina este envío
    private LocalDate fechaProgramada;
    private LocalDate fechaEstimada;
    private Double costoTotal;

    // dirección de destino
    private String calle;
    private String numero;
    private String comuna;
    private String ciudad;
    private String region;
    private String referencia;

    // dimensiones del paquete
    private Double pesoKg;
    private Double altoCm;
    private Double anchoCm;
    private Double largoCm;
    private String descripcionContenido;

    public EnvioRequestDTO() {
        this.idPedidoRef = "";
        this.costoTotal = 0.0;
        this.pesoKg = 0.0;
        this.altoCm = 0.0;
        this.anchoCm = 0.0;
        this.largoCm = 0.0;
    }

    public String getIdPedidoRef() { return idPedidoRef; }
    public void setIdPedidoRef(String idPedidoRef) { this.idPedidoRef = idPedidoRef; }
    public LocalDate getFechaProgramada() { return fechaProgramada; }
    public void setFechaProgramada(LocalDate fechaProgramada) { this.fechaProgramada = fechaProgramada; }
    public LocalDate getFechaEstimada() { return fechaEstimada; }
    public void setFechaEstimada(LocalDate fechaEstimada) { this.fechaEstimada = fechaEstimada; }
    public Double getCostoTotal() { return costoTotal; }
    public void setCostoTotal(Double costoTotal) { this.costoTotal = costoTotal; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getComuna() { return comuna; }
    public void setComuna(String comuna) { this.comuna = comuna; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
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
}
