package com.example.ms_pedidos_smartlogix.service;

import com.example.ms_pedidos_smartlogix.dto.ClienteRequestDTO;
import com.example.ms_pedidos_smartlogix.dto.ClienteResponseDTO;
import com.example.ms_pedidos_smartlogix.model.Cliente;
import com.example.ms_pedidos_smartlogix.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponseDTO crearCliente(ClienteRequestDTO dto) {
        if (clienteRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("El correo " + dto.getCorreo() + " ya existe");
        }

        Cliente cliente = new Cliente(
            dto.getNombre(),
            dto.getApellido(),
            dto.getCorreo(),
            dto.getTelefono(),
            dto.getDocumento()
        );

        Cliente clienteGuardado = clienteRepository.save(cliente);
        return convertToResponseDTO(clienteGuardado);
    }

    public List<ClienteResponseDTO> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO obtenerClientePorId(Long idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + idCliente + " no existe"));
        return convertToResponseDTO(cliente);
    }

    public ClienteResponseDTO actualizarCliente(Long idCliente, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + idCliente + " no existe"));

        if (!cliente.getCorreo().equals(dto.getCorreo()) && clienteRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("El correo " + dto.getCorreo() + " ya existe");
        }

        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setCorreo(dto.getCorreo());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDocumento(dto.getDocumento());

        Cliente clienteActualizado = clienteRepository.save(cliente);
        return convertToResponseDTO(clienteActualizado);
    }

    public void eliminarCliente(Long idCliente) {
        if (!clienteRepository.existsById(idCliente)) {
            throw new RuntimeException("Cliente con ID " + idCliente + " no existe");
        }
        clienteRepository.deleteById(idCliente);
    }

    private ClienteResponseDTO convertToResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
            cliente.getIdCliente(),
            cliente.getNombre(),
            cliente.getApellido(),
            cliente.getCorreo(),
            cliente.getTelefono(),
            cliente.getDocumento(),
            cliente.getFechaCreacion()
        );
    }
}
