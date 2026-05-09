package com.smartlogix.bff.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearRolRequest {
    
    @NotBlank(message = "El nombre del rol es obligatorio")
    private String nombreRol;
    
    private String descripcion;
    
    private Boolean activo;
}
