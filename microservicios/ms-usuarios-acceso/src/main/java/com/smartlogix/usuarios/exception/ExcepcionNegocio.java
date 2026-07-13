package com.smartlogix.usuarios.exception;

/** Errores de reglas de negocio (credenciales inválidas, duplicados, etc.). */
public class ExcepcionNegocio extends RuntimeException {
    public ExcepcionNegocio(String mensaje) {
        super(mensaje);
    }
}
