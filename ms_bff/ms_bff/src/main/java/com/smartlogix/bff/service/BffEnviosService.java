package com.smartlogix.bff.service;

import com.smartlogix.bff.client.EnviosClient;
import com.smartlogix.bff.client.MsEnviosSeguimientoClient;
import com.smartlogix.bff.client.MsEnviosTransportistasClient;
import com.smartlogix.bff.exception.ExternalServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BffEnviosService {

    private final EnviosClient enviosClient;
    private final MsEnviosSeguimientoClient seguimientoClient;
    private final MsEnviosTransportistasClient transportistasClient;
    private final com.smartlogix.bff.client.PedidosClient pedidosClient;

    public BffEnviosService(EnviosClient enviosClient,
                            MsEnviosSeguimientoClient seguimientoClient,
                            MsEnviosTransportistasClient transportistasClient,
                            com.smartlogix.bff.client.PedidosClient pedidosClient) {
        this.enviosClient = enviosClient;
        this.seguimientoClient = seguimientoClient;
        this.transportistasClient = transportistasClient;
        this.pedidosClient = pedidosClient;
    }

    public List<?> listarEnvios() {
        try {
            return enviosClient.listarEnvios();
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo conectar con el servicio de envíos", e);
        }
    }

    private String extraerEstadoPedido(Object pedido) {
        if (pedido instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) pedido;
            if (map.containsKey("data")) {
                Object data = map.get("data");
                if (data instanceof Map) {
                    Map<?, ?> dataMap = (Map<?, ?>) data;
                    Object est = dataMap.get("estadoActual");
                    if (est == null) {
                        est = dataMap.get("estado");
                    }
                    if (est == null) {
                        est = dataMap.get("estadoPedido");
                    }
                    return est != null ? est.toString() : "";
                }
            }
            Object est = map.get("estadoActual");
            if (est == null) {
                est = map.get("estado");
            }
            if (est == null) {
                est = map.get("estadoPedido");
            }
            return est != null ? est.toString() : "";
        }
        return "";
    }

    public Object crearEnvio(Map<String, Object> request) {
        try {
            Object idRefObj = request.get("idPedidoRef");
            if (idRefObj == null || idRefObj.toString().isBlank()) {
                throw new com.smartlogix.bff.exception.BusinessException("El idPedidoRef es obligatorio.");
            }
            Long idPedidoRef = Long.valueOf(idRefObj.toString());

            Object pedido = pedidosClient.buscarPedido(idPedidoRef);
            String estado = extraerEstadoPedido(pedido);

            if (!"CONFIRMADO".equalsIgnoreCase(estado)) {
                throw new com.smartlogix.bff.exception.BusinessException(
                        "Solo se pueden crear envíos para pedidos confirmados. Estado actual: " + estado);
            }

            return enviosClient.crearEnvio(request);

        } catch (feign.FeignException e) {
            String body = e.contentUTF8();
            String mensaje = e.getMessage();
            if (body != null && !body.isBlank()) {
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper =
                            new com.fasterxml.jackson.databind.ObjectMapper();
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
            throw new ExternalServiceException("Error desde ms_envios: " + mensaje, e);
        } catch (com.smartlogix.bff.exception.BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo crear el envío: " + e.getMessage(), e);
        }
    }

    public Object buscarEnvio(String idEnvio) {
        try {
            return enviosClient.buscarEnvio(idEnvio);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener el envío", e);
        }
    }

    public Object buscarEnvioPorPedido(String idPedidoRef) {
        try {
            return enviosClient.buscarEnvioPorPedido(idPedidoRef);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener el envío por pedido", e);
        }
    }

    public Object asignarTransportista(String idEnvio, Map<String, Object> request) {
        try {
            Object idTransObj = request.get("idTransportista");
            if (idTransObj == null || idTransObj.toString().isBlank()) {
                throw new com.smartlogix.bff.exception.BusinessException("El idTransportista es obligatorio.");
            }
            String idTransportista = idTransObj.toString();
            return enviosClient.asignarTransportista(idEnvio, idTransportista);
        } catch (com.smartlogix.bff.exception.BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo asignar el transportista", e);
        }
    }

    public Object actualizarEstadoEnvio(String idEnvio, Map<String, Object> request) {
        try {
            Object estadoObj = request.get("estadoEnvio");
            if (estadoObj == null) {
                estadoObj = request.get("estado");
            }

            if (estadoObj == null || estadoObj.toString().isBlank()) {
                throw new com.smartlogix.bff.exception.BusinessException("Debe indicar el nuevo estado del envío.");
            }

            String estado = estadoObj.toString().trim().toUpperCase();

            if (!List.of("PENDIENTE", "PENDIENTE_ASIGNACION", "ASIGNADO", "EN_TRANSITO", "ENTREGADO", "INCIDENCIA")
                    .contains(estado)) {
                throw new com.smartlogix.bff.exception.BusinessException("Estado de envío no válido: " + estado);
            }

            String observacion = request.get("observacion") != null
                    ? request.get("observacion").toString()
                    : "Cambio de estado desde BFF";

            Map<String, Object> requestMsEnvios = new java.util.HashMap<>();
            requestMsEnvios.put("estadoEnvio", estado);
            requestMsEnvios.put("observacion", observacion);

            return enviosClient.actualizarEstadoEnvio(idEnvio, requestMsEnvios);
        } catch (feign.FeignException e) {
            String body = e.contentUTF8();
            String mensaje = e.getMessage();
            if (body != null && !body.isBlank()) {
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper =
                            new com.fasterxml.jackson.databind.ObjectMapper();
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
            throw new ExternalServiceException("Error desde ms_envios al cambiar estado: " + mensaje, e);
        } catch (com.smartlogix.bff.exception.BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo actualizar el estado del envío: " + e.getMessage(), e);
        }
    }

    public List<?> listarSeguimiento(String idEnvio) {
        try {
            return seguimientoClient.listarPorEnvio(idEnvio);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener el seguimiento", e);
        }
    }

    public List<?> listarTransportistasActivos() {
        try {
            return transportistasClient.listarActivos();
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener la lista de transportistas", e);
        }
    }
}
