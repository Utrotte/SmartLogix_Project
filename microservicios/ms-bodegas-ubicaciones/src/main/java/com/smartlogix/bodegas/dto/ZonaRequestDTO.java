package com.smartlogix.bodegas.dto;

import jakarta.validation.constraints.NotBlank;

public class ZonaRequestDTO {

    @NotBlank
    private String nombre;

    public ZonaRequestDTO() {}
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
