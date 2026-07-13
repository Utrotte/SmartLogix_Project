package com.smartlogix.bff.dto;

public record ExistenciaInventarioDTO(
        Long idProducto,
        Long idBodega,
        int stockActual,
        int stockReservado,
        int stockDisponible
) {
}
