package com.smartlogix.pedidos.dto.avisos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearAvisoRequest {
    private String tipoAviso;
    private String canal;
    private String destinatario;
    private String asunto;
    private String mensaje;
    private Long idReferencia;
    private String tipoReferencia;
}
