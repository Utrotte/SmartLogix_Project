package cl.programadormaldito.ms_envios.client;

import cl.programadormaldito.ms_envios.integration.dto.ExistenciaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Cliente Feign para comunicarse con ms-inventario
@FeignClient(
    name = "ms-inventario",
    configuration = cl.programadormaldito.ms_envios.config.FeignConfig.class
)
public interface InventarioClient {

    // Obtiene disponibilidad de stock para un producto en una bodega
    @GetMapping("/api/inventario/existencias/producto/{idProducto}/bodega/{idBodega}")
    ExistenciaResponseDTO obtenerExistencia(
        @PathVariable("idProducto") Long idProducto,
        @PathVariable("idBodega") Long idBodega
    );
}
