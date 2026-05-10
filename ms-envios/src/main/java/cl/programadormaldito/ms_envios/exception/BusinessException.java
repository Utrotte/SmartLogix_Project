package cl.programadormaldito.ms_envios.exception;

// Excepción para violaciones de reglas de negocio
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
