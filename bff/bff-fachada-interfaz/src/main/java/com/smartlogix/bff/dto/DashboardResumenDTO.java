package com.smartlogix.bff.dto;

public record DashboardResumenDTO(
        int totalClientes,
        int totalProductos,
        int totalBodegas,
        int totalPedidos,
        int totalAvisos,
        int totalPagos,
        int totalEnvios,
        int totalTransportistas,
        int totalRutas,
        int stockActualTotal,
        int stockReservadoTotal,
        int stockDisponibleTotal,
        String mensaje
) {
}