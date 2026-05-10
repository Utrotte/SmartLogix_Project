package cl.programadormaldito.ms_envios.dto;

// datos para registrar un evento en el historial de seguimiento de un envío
public class SeguimientoRequestDTO {

    private String idEnvio;
    private String descripcion;     // ej: "Paquete en camino al domicilio"
    private String ubicacion;       // ej: "Pudahuel, Santiago"
    private String nuevoEstadoEnvio; // si viene con valor, actualiza el estado del envío

    public SeguimientoRequestDTO() {
        this.idEnvio = "";
        this.descripcion = "";
        this.ubicacion = "";
        this.nuevoEstadoEnvio = "";
    }

    public String getIdEnvio() { return idEnvio; }
    public void setIdEnvio(String idEnvio) { this.idEnvio = idEnvio; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getNuevoEstadoEnvio() { return nuevoEstadoEnvio; }
    public void setNuevoEstadoEnvio(String nuevoEstadoEnvio) { this.nuevoEstadoEnvio = nuevoEstadoEnvio; }
}
