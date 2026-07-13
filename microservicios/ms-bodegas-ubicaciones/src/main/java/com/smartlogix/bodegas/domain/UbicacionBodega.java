package com.smartlogix.bodegas.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ubicaciones_bodega")
public class UbicacionBodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idBodega;

    private Long idZona;

    private String codigo;

    private String estado;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public UbicacionBodega() {}

    public UbicacionBodega(Long idBodega, Long idZona, String codigo, String estado) {
        this.idBodega = idBodega;
        this.idZona = idZona;
        this.codigo = codigo;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public Long getIdBodega() { return idBodega; }
    public void setIdBodega(Long idBodega) { this.idBodega = idBodega; }
    public Long getIdZona() { return idZona; }
    public void setIdZona(Long idZona) { this.idZona = idZona; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
