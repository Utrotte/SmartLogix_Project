package com.smartlogix.bff.dto;

public record AvisoResumenDTO(
        Long id,
        String tipo_aviso,
        String canal,
        String destinatario,
        String asunto,
        String mensaje,
        String estado_aviso,
        Long id_referencia,
        String tipo_referencia
) {
}
