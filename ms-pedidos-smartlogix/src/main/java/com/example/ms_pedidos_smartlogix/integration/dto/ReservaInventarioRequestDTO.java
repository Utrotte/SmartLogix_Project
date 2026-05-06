package com.example.ms_pedidos_smartlogix.integration.dto;

import java.util.List;

public class ReservaInventarioRequestDTO {

    private Long idPedidoRef;
    private String codigoPedidoRef;
    private List<ReservaInventarioDetalleRequestDTO> detalles;

    public ReservaInventarioRequestDTO() {
    }

    public ReservaInventarioRequestDTO(Long idPedidoRef, String codigoPedidoRef, List<ReservaInventarioDetalleRequestDTO> detalles) {
        this.idPedidoRef = idPedidoRef;
        this.codigoPedidoRef = codigoPedidoRef;
        this.detalles = detalles;
    }

    public Long getIdPedidoRef() {
        return idPedidoRef;
    }

    public void setIdPedidoRef(Long idPedidoRef) {
        this.idPedidoRef = idPedidoRef;
    }

    public String getCodigoPedidoRef() {
        return codigoPedidoRef;
    }

    public void setCodigoPedidoRef(String codigoPedidoRef) {
        this.codigoPedidoRef = codigoPedidoRef;
    }

    public List<ReservaInventarioDetalleRequestDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<ReservaInventarioDetalleRequestDTO> detalles) {
        this.detalles = detalles;
    }
}
