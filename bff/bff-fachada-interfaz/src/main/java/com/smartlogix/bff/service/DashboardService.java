package com.smartlogix.bff.service;

import com.smartlogix.bff.client.AvisosClient;
import com.smartlogix.bff.client.PagoClient;
import com.smartlogix.bff.client.EnvioClient;
import com.smartlogix.bff.client.TransportistaClient;
import com.smartlogix.bff.client.BodegaClient;
import com.smartlogix.bff.client.CatalogoClient;
import com.smartlogix.bff.client.ClienteClient;
import com.smartlogix.bff.client.InventarioClient;
import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.dto.AvisoResumenDTO;
import com.smartlogix.bff.dto.BodegaResumenDTO;
import com.smartlogix.bff.dto.DashboardResumenDTO;
import com.smartlogix.bff.dto.ExistenciaInventarioDTO;
import com.smartlogix.bff.dto.ProductoResumenDTO;
import com.smartlogix.bff.dto.ClienteResumenDTO;
import com.smartlogix.bff.dto.PedidoResumenDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final ClienteClient clienteClient;
    private final CatalogoClient catalogoClient;
    private final BodegaClient bodegaClient;
    private final InventarioClient inventarioClient;
    private final PedidoClient pedidoClient;
    private final AvisosClient avisosClient;
    private final PagoClient pagoClient;
    private final EnvioClient envioClient;
    private final TransportistaClient transportistaClient;

    public DashboardService(ClienteClient clienteClient,
                            CatalogoClient catalogoClient,
                            BodegaClient bodegaClient,
                            InventarioClient inventarioClient,
                            PedidoClient pedidoClient,
                            AvisosClient avisosClient,
                            PagoClient pagoClient,
                            EnvioClient envioClient,
                            TransportistaClient transportistaClient) {
        this.clienteClient = clienteClient;
        this.catalogoClient = catalogoClient;
        this.bodegaClient = bodegaClient;
        this.inventarioClient = inventarioClient;
        this.pedidoClient = pedidoClient;
        this.avisosClient = avisosClient;
        this.pagoClient = pagoClient;
        this.envioClient = envioClient;
        this.transportistaClient = transportistaClient;
    }

    public DashboardResumenDTO obtenerResumen() {
        List<ClienteResumenDTO> clientes = clienteClient.listarClientes();
        List<ProductoResumenDTO> productos = catalogoClient.listarProductos();
        List<BodegaResumenDTO> bodegas = bodegaClient.listarBodegas();
        List<ExistenciaInventarioDTO> existencias = inventarioClient.listarExistencias();
        List<PedidoResumenDTO> pedidos = pedidoClient.listarPedidos();
        List<AvisoResumenDTO> avisos = avisosClient.listarAvisos();

        int totalClientes = clientes == null ? 0 : clientes.size();
        int totalProductos = productos == null ? 0 : productos.size();
        int totalBodegas = bodegas == null ? 0 : bodegas.size();
        int totalPedidos = pedidos == null ? 0 : pedidos.size();
        int totalAvisos = avisos == null ? 0 : avisos.size();

        List<Object> pagos = pagoClient.listarPagos();
        List<Object> envios = envioClient.listarEnvios();
        List<Object> transportistas = transportistaClient.listarTransportistas();
        List<Object> rutas = transportistaClient.listarRutas();

        int totalPagos = pagos == null ? 0 : pagos.size();
        int totalEnvios = envios == null ? 0 : envios.size();
        int totalTransportistas = transportistas == null ? 0 : transportistas.size();
        int totalRutas = rutas == null ? 0 : rutas.size();

        int stockActualTotal = 0;
        int stockReservadoTotal = 0;
        int stockDisponibleTotal = 0;
        if (existencias != null) {
            for (ExistenciaInventarioDTO e : existencias) {
                stockActualTotal += e.stockActual();
                stockReservadoTotal += e.stockReservado();
                stockDisponibleTotal += e.stockDisponible();
            }
        }

        String mensaje = "Resumen general generado desde el BFF";

        return new DashboardResumenDTO(
            totalClientes,
            totalProductos,
            totalBodegas,
            totalPedidos,
            totalAvisos,
            totalPagos,
            totalEnvios,
            totalTransportistas,
            totalRutas,
            stockActualTotal,
            stockReservadoTotal,
            stockDisponibleTotal,
            mensaje
        );
    }

}
