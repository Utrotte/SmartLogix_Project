package cl.programadormaldito.ms_envios.dto;

public class CambiarEstadoEnvioRequest {
    private String estadoEnvio;
    private String observacion;

    public CambiarEstadoEnvioRequest() {}

    public CambiarEstadoEnvioRequest(String estadoEnvio, String observacion) {
        this.estadoEnvio = estadoEnvio;
        this.observacion = observacion;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
