package com.example.gomesrodris.archburgers.domain.usecases;

import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.repositories.ClienteRepository;
import com.example.gomesrodris.archburgers.domain.usecaseports.ClienteUseCasesPort;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;

import java.util.List;

public class ClienteUseCases implements ClienteUseCasesPort {
    private final ClienteRepository clienteRepository;

    public ClienteUseCases(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente getClienteByCpf(Cpf cpf) {
        return clienteRepository.getClienteByCpf(cpf);
    }

    @Override
    public List<Cliente> listTodosClientes() {
        return clienteRepository.listarTodosClientes();
    }
}
