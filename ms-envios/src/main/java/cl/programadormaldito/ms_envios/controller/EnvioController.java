package cl.programadormaldito.ms_envios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.programadormaldito.ms_envios.dto.EnvioRequestDTO;
import cl.programadormaldito.ms_envios.dto.EnvioResponseDTO;
import cl.programadormaldito.ms_envios.service.EnvioService;

import cl.programadormaldito.ms_envios.dto.CambiarEstadoEnvioRequest;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    // POST /envios — crea el envío junto a su dirección y paquete
    @PostMapping
    public EnvioResponseDTO envioCrear(@RequestBody EnvioRequestDTO dto) {
        return this.envioService.envioCrear(dto);
    }

    // GET /envios — lista todos los envíos registrados
    @GetMapping
    public List<EnvioResponseDTO> envioListar() {
        return this.envioService.envioListar();
    }

    // GET /envios/{id}
    @GetMapping("/{id}")
    public EnvioResponseDTO envioBuscarPorId(@PathVariable String id) {
        return this.envioService.envioBuscarPorId(id);
    }

    // GET /envios/pedido/{idPedidoRef} — busca el envío a partir del pedido que lo originó
    @GetMapping("/pedido/{idPedidoRef}")
    public EnvioResponseDTO envioBuscarPorPedido(@PathVariable String idPedidoRef) {
        return this.envioService.envioBuscarPorPedido(idPedidoRef);
    }

    // PUT /api/envios/{id}/estado — actualiza el ciclo de vida del envío
    @PutMapping("/{id}/estado")
    public EnvioResponseDTO envioActualizarEstado(
            @PathVariable String id, 
            @RequestBody CambiarEstadoEnvioRequest request) {
        System.out.println("MS_ENVIOS -> PUT cambiar estado ID: " + id);
        System.out.println("MS_ENVIOS -> Request estado: " + request.getEstadoEnvio());
        return this.envioService.envioActualizarEstado(id, request.getEstadoEnvio(), request.getObservacion());
    }

    // PUT /envios/{idEnvio}/asignar-transportista/{idTransportista}
    // asigna el transportista y cambia el estado a ASIGNADO
    @PutMapping("/{idEnvio}/asignar-transportista/{idTransportista}")
    public EnvioResponseDTO envioAsignarTransportista(
            @PathVariable String idEnvio,
            @PathVariable String idTransportista) {
        return this.envioService.envioAsignarTransportista(idEnvio, idTransportista);
    }
}
