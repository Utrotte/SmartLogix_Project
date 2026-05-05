package com.smartlogix.inventario_service.exception;

// Excepción para cuando un recurso no es encontrado (HTTP 404)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
