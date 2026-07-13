package com.smartlogix.catalogo.exception;

/**
 * Excepción lanzada cuando no se encuentra un recurso solicitado en el catálogo.
 */
public class ExcepcionRecursoNoEncontrado extends RuntimeException {

    public ExcepcionRecursoNoEncontrado(String mensaje) {
        super(mensaje);
    }
}
