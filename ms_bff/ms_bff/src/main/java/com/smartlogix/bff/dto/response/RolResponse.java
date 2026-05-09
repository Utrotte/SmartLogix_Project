package com.smartlogix.bff.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolResponse {
    
    private Long idRol;
    private String nombreRol;
    private String descripcion;
    private Boolean activo;
}
