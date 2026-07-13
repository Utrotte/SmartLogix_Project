package com.smartlogix.bodegas.dto;

public class UbicacionResponseDTO {

    private Long id;
    private Long idBodega;
    private Long idZona;
    private String codigo;
    private String estado;

    public UbicacionResponseDTO() {}

    public UbicacionResponseDTO(Long id, Long idBodega, Long idZona, String codigo, String estado) {
        this.id = id; this.idBodega = idBodega; this.idZona = idZona; this.codigo = codigo; this.estado = estado;
    }

    public Long getId() { return id; }
    public Long getIdBodega() { return idBodega; }
    public Long getIdZona() { return idZona; }
    public String getCodigo() { return codigo; }
    public String getEstado() { return estado; }
}
