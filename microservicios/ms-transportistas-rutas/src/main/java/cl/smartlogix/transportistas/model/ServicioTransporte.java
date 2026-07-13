package cl.smartlogix.transportistas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "servicio_transporte")
public class ServicioTransporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicioTransporte;

    @ManyToOne
    @JoinColumn(name = "id_transportista", nullable = false)
    private Transportista transportista;

    @Column(nullable = false, length = 80)
    private String nombreServicio;

    @Column(length = 150)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;

    public ServicioTransporte() {
    }

    public Long getIdServicioTransporte() {
        return idServicioTransporte;
    }

    public void setIdServicioTransporte(Long idServicioTransporte) {
        this.idServicioTransporte = idServicioTransporte;
    }

    public Transportista getTransportista() {
        return transportista;
    }

    public void setTransportista(Transportista transportista) {
        this.transportista = transportista;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}