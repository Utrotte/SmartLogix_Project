package cl.programadormaldito.ms_envios.model;

import java.util.List;

import cl.programadormaldito.ms_envios.util.GeneradorStringUtil;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// empresa o persona que realiza la entrega física de los paquetes
@Entity
@Table(name = "transportista")
public class Transportista {

    @Id
    @Column(name = "t_id", length = 40, nullable = false)
    private String id;

    @Column(name = "t_nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "t_telefono", length = 20)
    private String telefono;

    @Column(name = "t_correo", length = 100)
    private String correo;

    // ej: "EXPRESS", "ECONOMICO", "MISMO_DIA"
    @Column(name = "t_tipo_servicio", length = 50)
    private String tipoServicio;

    // false = inactivo, no recibe nuevos envíos
    @Column(name = "t_activo", nullable = false)
    private boolean activo;

    // un transportista puede tener muchos envíos asignados
    @OneToMany(mappedBy = "transportista", cascade = CascadeType.ALL)
    private List<Envio> envios;

    public Transportista() {
        this.id = GeneradorStringUtil.generarID();
        this.nombre = "";
        this.telefono = "";
        this.correo = "";
        this.tipoServicio = "";
        this.activo = true; // por defecto queda activo al crearse
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
    public List<Envio> getEnvios() { return envios; }
    public void setEnvios(List<Envio> envios) { this.envios = envios; }
}
