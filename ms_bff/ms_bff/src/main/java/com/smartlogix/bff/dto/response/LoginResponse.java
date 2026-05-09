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
public class LoginResponse {
    
    private Long idUsuario;
    private String nombre;
    private String correo;
    private List<String> roles;
    private String tokenReferencia;
    private LocalDateTime fechaExpiracion;
    private String mensaje;
}
