package cl.smartlogix.pagos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comprobante_pago")
public class ComprobantePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComprobantePago;

    @OneToOne
    @JoinColumn(name = "id_pago", nullable = false)
    private Pago pago;

    @Column(nullable = false, length = 100)
    private String numeroComprobante;

    @Column(length = 250)
    private String urlDocumento;

    @Column(nullable = false)
    private LocalDateTime fechaEmision = LocalDateTime.now();

    public ComprobantePago() {
    }

    public Long getIdComprobantePago() {
        return idComprobantePago;
    }

    public void setIdComprobantePago(Long idComprobantePago) {
        this.idComprobantePago = idComprobantePago;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public String getUrlDocumento() {
        return urlDocumento;
    }

    public void setUrlDocumento(String urlDocumento) {
        this.urlDocumento = urlDocumento;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
}