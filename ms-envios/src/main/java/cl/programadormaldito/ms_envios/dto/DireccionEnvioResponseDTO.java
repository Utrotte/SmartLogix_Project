package cl.programadormaldito.ms_envios.dto;

/**
 * Dirección de entrega expuesta en las respuestas de envío (alineado con el frontend).
 */
public class DireccionEnvioResponseDTO {

    private String calle;
    private String ciudad;
    private String region;
    private String codigoPostal;

    public DireccionEnvioResponseDTO() {
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
}
