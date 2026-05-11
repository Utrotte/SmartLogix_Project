package com.smartlogix.bff.service;

import com.smartlogix.bff.client.PedidosClient;
import com.smartlogix.bff.exception.ExternalServiceException;
import feign.FeignException;
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
        } catch (FeignException e) {
            String body = e.contentUTF8();
            System.err.println("Error BFF al listar pedidos (Feign): status=" + e.status() + " body=" + body);
            throw new ExternalServiceException("Error desde ms_pedidos al listar pedidos: " + body, e);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo conectar con el servicio de pedidos: " + e.getMessage(), e);
        }
    }

    public List<?> listarPedidosPorCliente(Long idCliente) {
        try {
            return pedidosClient.listarPedidosPorCliente(idCliente);
        } catch (FeignException e) {
            String body = e.contentUTF8();
            System.err.println("Error BFF al listar pedidos por cliente (Feign): status=" + e.status() + " body=" + body);
            throw new ExternalServiceException("Error desde ms_pedidos al buscar pedidos del cliente: " + body, e);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener los pedidos del cliente: " + e.getMessage(), e);
        }
    }

    public Object crearPedido(Map<String, Object> request) {
        System.out.println("BFF -> Request crear pedido: " + request);
        try {
            Object response = pedidosClient.crearPedido(request);
            System.out.println("BFF -> Respuesta crear pedido: " + response);
            return response;
        } catch (FeignException e) {
            String body = e.contentUTF8();
            System.err.println("Error BFF al crear pedido (Feign): status=" + e.status() + " body=" + body);
            throw new ExternalServiceException("Error desde ms_pedidos al crear pedido: " + body, e);
        } catch (Exception e) {
            System.err.println("Error BFF al crear pedido: " + e.getMessage());
            throw new ExternalServiceException("No se pudo crear el pedido: " + e.getMessage(), e);
        }
    }

    public Object buscarPedido(Long idPedido) {
        try {
            return pedidosClient.buscarPedido(idPedido);
        } catch (FeignException e) {
            String body = e.contentUTF8();
            System.err.println("Error BFF al buscar pedido (Feign): status=" + e.status() + " body=" + body);
            throw new ExternalServiceException("Error desde ms_pedidos al buscar pedido: " + body, e);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener el pedido: " + e.getMessage(), e);
        }
    }

    public Object actualizarPedido(Long idPedido, Map<String, Object> request) {
        try {
            return pedidosClient.actualizarPedido(idPedido, request);
        } catch (FeignException e) {
            String body = e.contentUTF8();
            System.err.println("Error BFF al actualizar pedido (Feign): status=" + e.status() + " body=" + body);
            throw new ExternalServiceException("Error desde ms_pedidos al actualizar pedido: " + body, e);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo actualizar el pedido: " + e.getMessage(), e);
        }
    }

    public Object cancelarPedido(Long idPedido) {
        try {
            return pedidosClient.cancelarPedido(idPedido);
        } catch (FeignException e) {
            String body = e.contentUTF8();
            System.err.println("Error BFF al cancelar pedido (Feign): status=" + e.status() + " body=" + body);
            throw new ExternalServiceException("Error desde ms_pedidos al cancelar pedido: " + body, e);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo cancelar el pedido: " + e.getMessage(), e);
        }
    }

    public Object cambiarEstadoPedido(Long idPedido, Map<String, Object> request) {
        try {
            System.out.println("BFF -> Cambiar estado pedido ID: " + idPedido);
            System.out.println("BFF -> Request recibido: " + request);
            
            Object estadoObj = request.get("estado");
            if (estadoObj == null) estadoObj = request.get("estadoPedido");
            if (estadoObj == null) estadoObj = request.get("estadoNuevo");
            if (estadoObj == null) estadoObj = request.get("nuevoEstado");

            if (estadoObj == null || estadoObj.toString().trim().isEmpty()) {
                throw new com.smartlogix.bff.exception.BusinessException("Debe indicar el nuevo estado del pedido.");
            }

            String estado = estadoObj.toString().trim().toUpperCase();

            String observacion = request.get("observacion") != null
                    ? request.get("observacion").toString()
                    : "Cambio de estado desde BFF";

            String usuarioResponsable = request.get("usuarioResponsable") != null
                    ? request.get("usuarioResponsable").toString()
                    : "SISTEMA";

            Map<String, Object> requestMsPedidos = new java.util.HashMap<>();
            requestMsPedidos.put("estado", estado);
            requestMsPedidos.put("observacion", observacion);
            requestMsPedidos.put("usuarioResponsable", usuarioResponsable);

            System.out.println("BFF -> Request normalizado para ms_pedidos: " + requestMsPedidos);

            return pedidosClient.cambiarEstadoPedido(idPedido, requestMsPedidos);

        } catch (FeignException e) {
            String body = e.contentUTF8();
            System.err.println("Feign error cambiar estado. Status: " + e.status());
            System.err.println("Feign body cambiar estado: " + body);
            
            String mensaje = e.getMessage();
            if (body != null && !body.isBlank()) {
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(body);
                    if (root.has("message")) {
                        mensaje = root.get("message").asText();
                    } else {
                        mensaje = body;
                    }
                } catch (Exception parseEx) {
                    mensaje = body;
                }
            }
            throw new ExternalServiceException("Error desde ms_pedidos: " + mensaje, e);
        } catch (com.smartlogix.bff.exception.BusinessException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Error inesperado cambiar estado: " + e.getMessage());
            throw new ExternalServiceException("No se pudo cambiar el estado del pedido: " + e.getMessage(), e);
        }
    }

    public void eliminarPedido(Long idPedido) {
        System.out.println("BFF Service -> Eliminar pedido: " + idPedido);
        try {
            pedidosClient.eliminarPedido(idPedido);
        } catch (FeignException e) {
            String body = e.contentUTF8();
            System.err.println("Error BFF al eliminar pedido (Feign): status=" + e.status() + " body=" + body);
            throw new ExternalServiceException("Error desde ms_pedidos al eliminar pedido: " + body, e);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo eliminar el pedido: " + e.getMessage(), e);
        }
    }
}
