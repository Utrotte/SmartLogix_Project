package cl.programadormaldito.ms_envios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.programadormaldito.ms_envios.model.Transportista;
import cl.programadormaldito.ms_envios.service.TransportistaService;

@RestController
@RequestMapping("/transportistas")
public class TransportistaController {

    @Autowired
    private TransportistaService transportistaService;

    // POST /transportistas — registra un nuevo transportista
    @PostMapping
    public void transportistaAlmacenar(@RequestBody Transportista transportista) {
        this.transportistaService.transportistaAlmacenar(transportista);
    }

    // GET /transportistas — devuelve todos (activos e inactivos)
    @GetMapping
    public List<Transportista> transportistaListar() {
        return this.transportistaService.transportistaListar();
    }

    // GET /transportistas/activos — solo los disponibles para recibir envíos
    @GetMapping("/activos")
    public List<Transportista> transportistaListarActivos() {
        return this.transportistaService.transportistaListarActivos();
    }
}
