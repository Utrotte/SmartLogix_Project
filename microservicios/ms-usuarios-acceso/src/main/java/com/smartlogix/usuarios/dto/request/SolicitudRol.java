package com.smartlogix.usuarios.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SolicitudRol {
    @NotBlank(message = "El nombre del rol es obligatorio")
    private String nombre;
    private String descripcion;
}
