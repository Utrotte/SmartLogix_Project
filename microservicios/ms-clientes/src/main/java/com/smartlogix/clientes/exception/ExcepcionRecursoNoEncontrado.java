package com.smartlogix.clientes.exception;

/**
 * Excepción lanzada cuando no se encuentra un recurso solicitado en clientes.
 */
public class ExcepcionRecursoNoEncontrado extends RuntimeException {

    public ExcepcionRecursoNoEncontrado(String mensaje) {
        super(mensaje);
    }
}
