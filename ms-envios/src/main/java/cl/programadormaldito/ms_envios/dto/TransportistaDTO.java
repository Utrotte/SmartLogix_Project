package cl.programadormaldito.ms_envios.dto;

// datos para crear o actualizar un transportista
public class TransportistaDTO {

    private String nombre;
    private String telefono;
    private String correo;
    private String tipoServicio; // ej: "EXPRESS", "ECONOMICO"
    private boolean activo;

    public TransportistaDTO() {
        this.nombre = "";
        this.telefono = "";
        this.correo = "";
        this.tipoServicio = "";
        this.activo = true;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
