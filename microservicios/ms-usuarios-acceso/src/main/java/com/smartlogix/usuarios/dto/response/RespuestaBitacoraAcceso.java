package com.smartlogix.usuarios.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class RespuestaBitacoraAcceso {
    private Long idBitacora;
    private Long idUsuario;
    private String correoIntento;
    private String tipoEvento;
    private String descripcion;
    private String ipOrigen;
    private LocalDateTime fechaEvento;
    private Boolean exitoso;
}
