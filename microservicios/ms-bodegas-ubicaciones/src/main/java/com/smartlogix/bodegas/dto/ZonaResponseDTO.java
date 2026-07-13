package com.smartlogix.bodegas.dto;

public class ZonaResponseDTO {

    private Long id;
    private Long idBodega;
    private String nombre;
    private boolean activo;

    public ZonaResponseDTO() {}

    public ZonaResponseDTO(Long id, Long idBodega, String nombre, boolean activo) {
        this.id = id; this.idBodega = idBodega; this.nombre = nombre; this.activo = activo;
    }

    public Long getId() { return id; }
    public Long getIdBodega() { return idBodega; }
    public String getNombre() { return nombre; }
    public boolean isActivo() { return activo; }
}
