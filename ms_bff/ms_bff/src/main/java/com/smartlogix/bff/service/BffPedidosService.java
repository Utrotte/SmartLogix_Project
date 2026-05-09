package com.smartlogix.bff.service;

import com.smartlogix.bff.client.PedidosClient;
import com.smartlogix.bff.exception.ExternalServiceException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class BffPedidosService {

    private final PedidosClient pedidosClient;

    public BffPedidosService(PedidosClient pedidosClient) {
        this.pedidosClient = pedidosClient;
    }

    public List<?> listarPedidos() {
        try {
            return pedidosClient.listarPedidos();
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo conectar con el servicio de pedidos", e);
        }
    }

    public Object crearPedido(Map<String, Object> request) {
        try {
            return pedidosClient.crearPedido(request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo crear el pedido", e);
        }
    }

    public Object buscarPedido(Long idPedido) {
        try {
            return pedidosClient.buscarPedido(idPedido);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener el pedido", e);
        }
    }

    public Object actualizarPedido(Long idPedido, Map<String, Object> request) {
        try {
            return pedidosClient.actualizarPedido(idPedido, request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo actualizar el pedido", e);
        }
    }

    public Object cancelarPedido(Long idPedido) {
        try {
            return pedidosClient.cancelarPedido(idPedido);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo cancelar el pedido", e);
        }
    }

    public Object cambiarEstadoPedido(Long idPedido, Map<String, Object> request) {
        try {
            return pedidosClient.cambiarEstadoPedido(idPedido, request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo cambiar el estado del pedido", e);
        }
    }
}
