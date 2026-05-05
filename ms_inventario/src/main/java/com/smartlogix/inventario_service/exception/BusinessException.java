package com.smartlogix.inventario_service.exception;

// Excepción para errores de lógica de negocio (HTTP 400)
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
