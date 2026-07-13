package com.smartlogix.catalogo.exception;

/**
 * Excepción para reglas de negocio violadas en operaciones del catálogo.
 */
public class ExcepcionNegocio extends RuntimeException {

    public ExcepcionNegocio(String mensaje) {
        super(mensaje);
    }
}
