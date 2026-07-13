package com.smartlogix.bodegas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UbicacionRequestDTO {

    @NotNull
    private Long idBodega;

    @NotNull
    private Long idZona;

    @NotBlank
    private String codigo;

    private String estado;

    public UbicacionRequestDTO() {}

    public Long getIdBodega() { return idBodega; }
    public void setIdBodega(Long idBodega) { this.idBodega = idBodega; }
    public Long getIdZona() { return idZona; }
    public void setIdZona(Long idZona) { this.idZona = idZona; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
