package com.smartlogix.inventario_service.exception;

// Excepción para cuando se intenta crear un recurso duplicado (HTTP 409)
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
