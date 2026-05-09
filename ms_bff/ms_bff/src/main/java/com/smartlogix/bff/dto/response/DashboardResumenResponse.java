package com.smartlogix.bff.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResumenResponse {
    
    private Long totalProductos;
    private Long totalPedidos;
    private Long totalEnvios;
    private Long stockCritico;
    private Long reservasPendientes;
    private List<String> serviciosActivos;
    private String mensaje;
}
