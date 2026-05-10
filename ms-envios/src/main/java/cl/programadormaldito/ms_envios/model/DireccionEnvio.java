package cl.programadormaldito.ms_envios.model;

import cl.programadormaldito.ms_envios.util.GeneradorStringUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// dirección de destino donde se entregará el paquete
@Entity
@Table(name = "direccion_envio")
public class DireccionEnvio {

    @Id
    @Column(name = "de_id", length = 40, nullable = false)
    private String id;

    @Column(name = "de_calle", length = 100)
    private String calle;

    @Column(name = "de_numero", length = 20)
    private String numero;

    @Column(name = "de_comuna", length = 80)
    private String comuna;

    @Column(name = "de_ciudad", length = 80)
    private String ciudad;

    @Column(name = "de_region", length = 80)
    private String region;

    // indicaciones adicionales para el repartidor
    @Column(name = "de_referencia", length = 255)
    private String referencia;

    // FK hacia el envío — la relación 1:1 se gestiona desde este lado
    @OneToOne
    @JoinColumn(name = "de_id_envio", nullable = false)
    private Envio envio;

    public DireccionEnvio() {
        this.id = GeneradorStringUtil.generarID();
        this.calle = "";
        this.numero = "";
        this.comuna = "";
        this.ciudad = "";
        this.region = "";
        this.referencia = "";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getComuna() { return comuna; }
    public void setComuna(String comuna) { this.comuna = comuna; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public Envio getEnvio() { return envio; }
    public void setEnvio(Envio envio) { this.envio = envio; }
}
