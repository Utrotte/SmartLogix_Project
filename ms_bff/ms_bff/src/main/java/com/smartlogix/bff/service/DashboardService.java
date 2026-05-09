package com.smartlogix.bff.service;

import com.smartlogix.bff.dto.response.DashboardResumenResponse;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final BffInventarioService inventarioService;
    private final BffPedidosService pedidosService;
    private final BffEnviosService enviosService;

    public DashboardService(BffInventarioService inventarioService,
                           BffPedidosService pedidosService,
                           BffEnviosService enviosService) {
        this.inventarioService = inventarioService;
        this.pedidosService = pedidosService;
        this.enviosService = enviosService;
    }

    public DashboardResumenResponse obtenerResumen() {
        
        Long totalProductos = 0L;
        Long totalPedidos = 0L;
        Long totalEnvios = 0L;
        Long stockCritico = 0L;
        Long reservasPendientes = 0L;
        List<String> serviciosActivos = new ArrayList<>();
        StringBuilder mensaje = new StringBuilder();
        
        // Intentar obtener datos de inventario
        try {
            List<?> productos = inventarioService.listarProductos();
            totalProductos = (long) (productos != null ? productos.size() : 0);
            serviciosActivos.add("INVENTARIO");
        } catch (Exception e) {
            mensaje.append("Inventario no disponible. ");
        }
        
        // Intentar obtener datos de pedidos
        try {
            List<?> pedidos = pedidosService.listarPedidos();
            totalPedidos = (long) (pedidos != null ? pedidos.size() : 0);
            serviciosActivos.add("PEDIDOS");
        } catch (Exception e) {
            mensaje.append("Pedidos no disponible. ");
        }
        
        // Intentar obtener datos de envíos
        try {
            List<?> envios = enviosService.listarEnvios();
            totalEnvios = (long) (envios != null ? envios.size() : 0);
            serviciosActivos.add("ENVIOS");
        } catch (Exception e) {
            mensaje.append("Envíos no disponible. ");
        }
        
        // Intentar obtener reservas
        try {
            List<?> reservas = inventarioService.listarReservas();
            reservasPendientes = (long) (reservas != null ? reservas.size() : 0);
        } catch (Exception e) {
            // Sin mensaje adicional para reservas
        }
        
        String mensajeFinal = mensaje.length() > 0 
                ? mensaje.toString().trim() 
                : "Dashboard actualizado correctamente";
        
        return DashboardResumenResponse.builder()
                .totalProductos(totalProductos)
                .totalPedidos(totalPedidos)
                .totalEnvios(totalEnvios)
                .stockCritico(stockCritico)
                .reservasPendientes(reservasPendientes)
                .serviciosActivos(serviciosActivos)
                .mensaje(mensajeFinal)
                .build();
    }
}
