package cl.programadormaldito.ms_envios.integration.service;

import cl.programadormaldito.ms_envios.client.PedidosClient;
import cl.programadormaldito.ms_envios.client.InventarioClient;
import cl.programadormaldito.ms_envios.exception.BusinessException;
import cl.programadormaldito.ms_envios.exception.IntegrationException;
import cl.programadormaldito.ms_envios.integration.dto.PedidoResponseDTO;
import cl.programadormaldito.ms_envios.integration.dto.ExistenciaResponseDTO;
import feign.FeignException;
import org.springframework.stereotype.Service;

// Servicio encargado de validaciones e integraciones con otros microservicios
@Service
public class EnvioIntegrationService {

    private final PedidosClient pedidosClient;
    private final InventarioClient inventarioClient;

    // Constructor Injection
    public EnvioIntegrationService(PedidosClient pedidosClient, InventarioClient inventarioClient) {
        this.pedidosClient = pedidosClient;
        this.inventarioClient = inventarioClient;
    }

    // Valida que un pedido existe y está en estado CONFIRMADO
    public PedidoResponseDTO validarPedidoParaEnvio(Long idPedido) {
        try {
            PedidoResponseDTO pedido = pedidosClient.obtenerPedido(idPedido);

            if (pedido == null) {
                throw new BusinessException("Pedido no encontrado: " + idPedido);
            }

            if (!"CONFIRMADO".equals(pedido.getEstadoActual())) {
                throw new BusinessException(
                    "Pedido debe estar en estado CONFIRMADO. Estado actual: " + 
                    pedido.getEstadoActual()
                );
            }

            return pedido;

        } catch (FeignException e) {
            // Cuando ms-pedidos no responde o retorna error HTTP
            if (e.status() == 404) {
                throw new BusinessException("Pedido no existe en ms-pedidos: " + idPedido);
            }
            throw new IntegrationException(
                "No se pudo comunicar con ms-pedidos. Intente más tarde. Error: " + 
                e.getMessage()
            );
        } catch (BusinessException e) {
            // Re-lanzar excepciones de negocio sin modificar
            throw e;
        } catch (Exception e) {
            throw new IntegrationException(
                "Error desconocido en integración con ms-pedidos: " + e.getMessage(), e
            );
        }
    }

    // Valida disponibilidad de stock en inventario para un producto
    public ExistenciaResponseDTO validarStockDisponible(Long idProducto, Long idBodega) {
        try {
            ExistenciaResponseDTO existencia = inventarioClient.obtenerExistencia(idProducto, idBodega);

            if (existencia == null) {
                throw new BusinessException(
                    "Producto " + idProducto + " no existe en bodega " + idBodega
                );
            }

            if (existencia.getStockDisponible() <= 0) {
                throw new BusinessException(
                    "Stock insuficiente. Disponible: " + existencia.getStockDisponible()
                );
            }

            return existencia;

        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new BusinessException(
                    "Producto " + idProducto + " no existe en bodega " + idBodega
                );
            }
            throw new IntegrationException(
                "No se pudo comunicar con ms-inventario. Intente más tarde. Error: " + 
                e.getMessage()
            );
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new IntegrationException(
                "Error desconocido en integración con ms-inventario: " + e.getMessage(), e
            );
        }
    }
}
