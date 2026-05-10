package cl.programadormaldito.ms_envios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.programadormaldito.ms_envios.dto.SeguimientoRequestDTO;
import cl.programadormaldito.ms_envios.model.SeguimientoEnvio;
import cl.programadormaldito.ms_envios.service.SeguimientoEnvioService;

@RestController
@RequestMapping("/seguimientos")
public class SeguimientoEnvioController {

    @Autowired
    private SeguimientoEnvioService seguimientoEnvioService;

    // POST /seguimientos — registra un nuevo evento en el historial del envío
    @PostMapping
    public void seguimientoRegistrar(@RequestBody SeguimientoRequestDTO dto) {
        this.seguimientoEnvioService.seguimientoRegistrar(dto);
    }

    // GET /seguimientos/envio/{idEnvio} — devuelve el historial completo ordenado por fecha
    @GetMapping("/envio/{idEnvio}")
    public List<SeguimientoEnvio> seguimientoListarPorEnvio(@PathVariable String idEnvio) {
        return this.seguimientoEnvioService.seguimientoListarPorEnvio(idEnvio);
    }
}
