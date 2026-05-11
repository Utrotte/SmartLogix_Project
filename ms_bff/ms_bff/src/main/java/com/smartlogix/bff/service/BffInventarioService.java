package com.smartlogix.bff.service;

import com.smartlogix.bff.client.InventarioClient;
import com.smartlogix.bff.exception.ExternalServiceException;
import feign.FeignException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class BffInventarioService {

    private final InventarioClient inventarioClient;

    public BffInventarioService(InventarioClient inventarioClient) {
        this.inventarioClient = inventarioClient;
    }

    // Categorías
    public List<?> listarCategorias() {
        try {
            return inventarioClient.listarCategorias();
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo conectar con el servicio de inventario", e);
        }
    }

    public Object crearCategoria(Map<String, Object> request) {
        try {
            return inventarioClient.crearCategoria(request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo crear la categoría", e);
        }
    }

    public Object buscarCategoria(Long id) {
        try {
            return inventarioClient.buscarCategoria(id);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener la categoría", e);
        }
    }

    public Object actualizarCategoria(Long id, Map<String, Object> request) {
        try {
            return inventarioClient.actualizarCategoria(id, request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo actualizar la categoría", e);
        }
    }

    public void desactivarCategoria(Long id) {
        try {
            inventarioClient.desactivarCategoria(id);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo desactivar la categoría", e);
        }
    }

    // Productos
    public List<?> listarProductos() {
        try {
            return inventarioClient.listarProductos();
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo conectar con el servicio de inventario", e);
        }
    }

    public Object crearProducto(Map<String, Object> request) {
        try {
            return inventarioClient.crearProducto(request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo crear el producto", e);
        }
    }

    public Object buscarProducto(Long id) {
        try {
            return inventarioClient.buscarProducto(id);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener el producto", e);
        }
    }

    public Object buscarProductoPorSku(String codigoSku) {
        try {
            return inventarioClient.buscarProductoPorSku(codigoSku);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener el producto por SKU", e);
        }
    }

    public Object actualizarProducto(Long id, Map<String, Object> request) {
        try {
            return inventarioClient.actualizarProducto(id, request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo actualizar el producto", e);
        }
    }

    public void desactivarProducto(Long id) {
        try {
            inventarioClient.desactivarProducto(id);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo desactivar el producto", e);
        }
    }

    // Bodegas
    public List<?> listarBodegas() {
        try {
            return inventarioClient.listarBodegas();
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo conectar con el servicio de inventario", e);
        }
    }

    public Object crearBodega(Map<String, Object> request) {
        try {
            return inventarioClient.crearBodega(request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo crear la bodega", e);
        }
    }

    public Object buscarBodega(Long id) {
        try {
            return inventarioClient.buscarBodega(id);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener la bodega", e);
        }
    }

    public Object actualizarBodega(Long id, Map<String, Object> request) {
        try {
            return inventarioClient.actualizarBodega(id, request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo actualizar la bodega", e);
        }
    }

    public void desactivarBodega(Long id) {
        try {
            inventarioClient.desactivarBodega(id);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo desactivar la bodega", e);
        }
    }

    // Existencias
    public Object crearExistencia(Map<String, Object> request) {
        try {
            return inventarioClient.crearExistencia(request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo crear la existencia", e);
        }
    }

    public List<?> consultarExistenciasPorProducto(Long idProducto) {
        try {
            return inventarioClient.consultarExistenciasPorProducto(idProducto);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo consultar existencias", e);
        }
    }

    public List<?> consultarExistenciasPorBodega(Long idBodega) {
        try {
            return inventarioClient.consultarExistenciasPorBodega(idBodega);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo consultar existencias", e);
        }
    }

    public Object consultarExistenciaPorProductoYBodega(Long idProducto, Long idBodega) {
        try {
            return inventarioClient.consultarExistenciaPorProductoYBodega(idProducto, idBodega);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo consultar la existencia", e);
        }
    }

    public Object ajustarStock(Long idExistencia, Map<String, Object> request) {
        System.out.println("BFF -> idExistencia ajustar stock: " + idExistencia);
        System.out.println("BFF -> Request ajustar stock: " + request);
        try {
            return inventarioClient.ajustarStock(idExistencia, request);
        } catch (FeignException e) {
            String body = e.contentUTF8();
            System.err.println("Error BFF al ajustar stock (Feign): status=" + e.status() + " body=" + body);
            throw new ExternalServiceException("Error desde ms_inventario al ajustar stock: " + body, e);
        } catch (Exception e) {
            System.err.println("Error BFF al ajustar stock: " + e.getMessage());
            throw new ExternalServiceException("No se pudo ajustar el stock: " + e.getMessage(), e);
        }
    }

    // Reservas
    public Object crearReserva(Map<String, Object> request) {
        try {
            return inventarioClient.crearReserva(request);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo crear la reserva", e);
        }
    }

    public List<?> listarReservas() {
        try {
            return inventarioClient.listarReservas();
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo listar reservas", e);
        }
    }

    public Object buscarReserva(Long idReserva) {
        try {
            return inventarioClient.buscarReserva(idReserva);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener la reserva", e);
        }
    }

    public List<?> buscarReservasPorPedido(Long idPedidoRef) {
        try {
            return inventarioClient.buscarReservasPorPedido(idPedidoRef);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener las reservas", e);
        }
    }

    public Object confirmarReserva(Long idReserva) {
        try {
            return inventarioClient.confirmarReserva(idReserva);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo confirmar la reserva", e);
        }
    }

    public Object liberarReserva(Long idReserva) {
        try {
            return inventarioClient.liberarReserva(idReserva);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo liberar la reserva", e);
        }
    }

    // Movimientos
    public List<?> listarMovimientos() {
        try {
            return inventarioClient.listarMovimientos();
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo listar movimientos", e);
        }
    }

    public Object buscarMovimiento(Long idMovimiento) {
        try {
            return inventarioClient.buscarMovimiento(idMovimiento);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo obtener el movimiento", e);
        }
    }

    public List<?> listarMovimientosPorProducto(Long idProducto) {
        try {
            return inventarioClient.listarMovimientosPorProducto(idProducto);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo listar movimientos", e);
        }
    }

    public List<?> listarMovimientosPorBodega(Long idBodega) {
        try {
            return inventarioClient.listarMovimientosPorBodega(idBodega);
        } catch (Exception e) {
            throw new ExternalServiceException("No se pudo listar movimientos", e);
        }
    }
}
