package com.example.gomesrodris.archburgers.domain.usecases;

import com.example.gomesrodris.archburgers.domain.datagateway.ClienteGateway;
import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;

import java.util.List;

public class ClienteUseCases {
    private final ClienteGateway clienteGateway;

    public ClienteUseCases(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }

    public Cliente getClienteByCpf(Cpf cpf) {
        return clienteGateway.getClienteByCpf(cpf);
    }

    public List<Cliente> listTodosClientes() {
        return clienteGateway.listarTodosClientes();
    }
}
