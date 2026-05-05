package com.smartlogix.inventario_service.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private LocalDateTime timestamp; // Momento del error

    private int status; // Código HTTP (ej: 404, 400, 500)

    private String error; // Tipo de error

    private String message; // Mensaje detallado

    private String path; // Ruta del endpoint que generó el error
}
