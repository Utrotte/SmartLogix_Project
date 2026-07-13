package cl.smartlogix.pagos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "validacion_pago")
public class ValidacionPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idValidacionPago;

    @ManyToOne
    @JoinColumn(name = "id_pago", nullable = false)
    private Pago pago;

    @Column(nullable = false, length = 50)
    private String resultado;

    @Column(length = 250)
    private String detalle;

    @Column(nullable = false)
    private LocalDateTime fechaValidacion = LocalDateTime.now();

    public ValidacionPago() {
    }

    public Long getIdValidacionPago() {
        return idValidacionPago;
    }

    public void setIdValidacionPago(Long idValidacionPago) {
        this.idValidacionPago = idValidacionPago;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public LocalDateTime getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(LocalDateTime fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }
}