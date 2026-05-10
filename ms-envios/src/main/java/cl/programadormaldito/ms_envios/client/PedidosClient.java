package cl.programadormaldito.ms_envios.client;

import cl.programadormaldito.ms_envios.integration.dto.PedidoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Cliente Feign para comunicarse con ms-pedidos-smartlogix
@FeignClient(
    name = "ms-pedidos-smartlogix",
    configuration = cl.programadormaldito.ms_envios.config.FeignConfig.class
)
public interface PedidosClient {

    // Obtiene detalles de un pedido por su ID
    @GetMapping("/api/pedidos/{idPedido}")
    PedidoResponseDTO obtenerPedido(@PathVariable("idPedido") Long idPedido);

    // Obtiene un pedido por su código
    @GetMapping("/api/pedidos/codigo/{codigoPedido}")
    PedidoResponseDTO obtenerPedidoPorCodigo(@PathVariable("codigoPedido") String codigoPedido);
}
