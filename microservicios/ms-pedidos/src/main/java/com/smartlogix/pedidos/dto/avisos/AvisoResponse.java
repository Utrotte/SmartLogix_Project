package com.smartlogix.pedidos.dto.avisos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvisoResponse {
    private Long id;
    private String tipo_aviso;
    private String canal;
    private String destinatario;
    private String asunto;
    private String mensaje;
    private String estado_aviso;
    private Long id_referencia;
    private String tipo_referencia;
}
