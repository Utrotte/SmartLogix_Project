package com.smartlogix.clientes.exception;

/**
 * Excepción para reglas de negocio violadas en operaciones de clientes.
 */
public class ExcepcionNegocio extends RuntimeException {

    public ExcepcionNegocio(String mensaje) {
        super(mensaje);
    }
}
