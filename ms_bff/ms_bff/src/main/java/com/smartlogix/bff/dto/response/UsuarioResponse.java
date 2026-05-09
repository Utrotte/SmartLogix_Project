package com.smartlogix.bff.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {
    
    private Long idUsuario;
    private String nombre;
    private String correo;
    private String estado;
    private List<String> roles;
    private LocalDateTime fechaCreacion;
}
