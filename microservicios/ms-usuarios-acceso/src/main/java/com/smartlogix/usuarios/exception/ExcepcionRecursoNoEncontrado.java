package com.smartlogix.usuarios.exception;

/** Se lanza cuando no se encuentra un recurso solicitado por id. */
public class ExcepcionRecursoNoEncontrado extends RuntimeException {
    public ExcepcionRecursoNoEncontrado(String mensaje) {
        super(mensaje);
    }
}
