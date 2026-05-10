package cl.programadormaldito.ms_envios.exception;

// Excepción para fallos en comunicación con otros microservicios
public class IntegrationException extends RuntimeException {

    public IntegrationException(String message) {
        super(message);
    }

    public IntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
