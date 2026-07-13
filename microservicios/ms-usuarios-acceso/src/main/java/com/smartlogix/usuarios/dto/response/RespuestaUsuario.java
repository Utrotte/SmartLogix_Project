package com.smartlogix.usuarios.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class RespuestaUsuario {
    private Long idUsuario;
    private String nombre;
    private String correo;
    private String estado;
    private List<String> roles;
}
