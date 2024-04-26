package com.example.gomesrodris.archburgers.domain.services;

import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.repositories.ClienteRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServices {
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteServices(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente getClienteByCpf(Cpf cpf) {
        return clienteRepository.getClienteByCpf(cpf);
    }
}
