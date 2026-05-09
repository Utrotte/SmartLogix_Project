package com.smartlogix.bff.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SesionResponse {
    
    private Long idSesion;
    private Long idUsuario;
    private String correoUsuario;
    private String estado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaExpiracion;
}
