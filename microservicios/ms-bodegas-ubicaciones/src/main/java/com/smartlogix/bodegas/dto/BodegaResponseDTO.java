package com.smartlogix.bodegas.dto;

public class BodegaResponseDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private boolean activo;

    public BodegaResponseDTO() {}

    public BodegaResponseDTO(Long id, String nombre, String direccion, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public boolean isActivo() { return activo; }
}
