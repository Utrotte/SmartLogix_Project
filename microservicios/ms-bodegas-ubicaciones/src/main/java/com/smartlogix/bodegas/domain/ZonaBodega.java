package com.smartlogix.bodegas.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "zonas_bodega")
public class ZonaBodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idBodega;

    private String nombre;

    private boolean activo = true;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public ZonaBodega() {}

    public ZonaBodega(Long idBodega, String nombre) {
        this.idBodega = idBodega;
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public Long getIdBodega() { return idBodega; }
    public void setIdBodega(Long idBodega) { this.idBodega = idBodega; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
