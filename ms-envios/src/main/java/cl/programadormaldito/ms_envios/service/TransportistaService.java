package cl.programadormaldito.ms_envios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.programadormaldito.ms_envios.model.Transportista;
import cl.programadormaldito.ms_envios.repository.TransportistaRepository;

@Service
public class TransportistaService {

    @Autowired
    private TransportistaRepository transportistaRepository;

    public void transportistaAlmacenar(Transportista transportista) {
        this.transportistaRepository.save(transportista);
    }

    public List<Transportista> transportistaListar() {
        return this.transportistaRepository.findAll();
    }

    // devuelve solo los que tienen activo=true, para la asignación de envíos
    public List<Transportista> transportistaListarActivos() {
        return this.transportistaRepository.findByActivoTrue();
    }

    public Transportista transportistaBuscarPorId(String id) {
        return this.transportistaRepository.findById(id).orElse(null);
    }
}
