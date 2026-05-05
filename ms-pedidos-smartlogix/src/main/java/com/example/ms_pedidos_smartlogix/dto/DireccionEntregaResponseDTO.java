package com.example.ms_pedidos_smartlogix.dto;

public class DireccionEntregaResponseDTO {
    private Long idDireccion;
    private Long idPedido;
    private String calle;
    private String numero;
    private String comuna;
    private String ciudad;
    private String region;
    private String codigoPostal;
    private String referencia;

    public DireccionEntregaResponseDTO() {
    }

    public DireccionEntregaResponseDTO(Long idDireccion, Long idPedido, String calle, String numero,
                                        String comuna, String ciudad, String region, String codigoPostal, String referencia) {
        this.idDireccion = idDireccion;
        this.idPedido = idPedido;
        this.calle = calle;
        this.numero = numero;
        this.comuna = comuna;
        this.ciudad = ciudad;
        this.region = region;
        this.codigoPostal = codigoPostal;
        this.referencia = referencia;
    }

    public Long getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(Long idDireccion) {
        this.idDireccion = idDireccion;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
