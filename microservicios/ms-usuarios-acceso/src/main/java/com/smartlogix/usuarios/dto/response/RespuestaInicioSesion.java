package com.smartlogix.usuarios.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/** Respuesta del login con token JWT según el README de arquitectura. */
@Data
@Builder
public class RespuestaInicioSesion {
    private String token;
    private String tipoToken;
    private Long expiraEnMinutos;
    private RespuestaUsuario usuario;
}
