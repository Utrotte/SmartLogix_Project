package cl.smartlogix.envios.model;

import jakarta.persistence.*;

@Entity
@Table(name = "direccion_envio")
public class DireccionEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDireccionEnvio;

    @OneToOne
    @JoinColumn(name = "id_envio", nullable = false)
    private Envio envio;

    @Column(nullable = false, length = 120)
    private String calle;

    @Column(nullable = false, length = 20)
    private String numero;

    @Column(nullable = false, length = 80)
    private String comuna;

    @Column(nullable = false, length = 80)
    private String ciudad;

    @Column(nullable = false, length = 80)
    private String region;

    @Column(length = 150)
    private String referencia;

    public DireccionEnvio() {
    }

    public Long getIdDireccionEnvio() {
        return idDireccionEnvio;
    }

    public void setIdDireccionEnvio(Long idDireccionEnvio) {
        this.idDireccionEnvio = idDireccionEnvio;
    }

    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
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

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}