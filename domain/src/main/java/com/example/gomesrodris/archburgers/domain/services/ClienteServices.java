package com.example.gomesrodris.archburgers.domain.services;

import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.repositories.ClienteRepository;
import com.example.gomesrodris.archburgers.domain.serviceports.ClienteServicesPort;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;

public class ClienteServices implements ClienteServicesPort {
    private final ClienteRepository clienteRepository;

    public ClienteServices(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente getClienteByCpf(Cpf cpf) {
        return clienteRepository.getClienteByCpf(cpf);
    }
}
