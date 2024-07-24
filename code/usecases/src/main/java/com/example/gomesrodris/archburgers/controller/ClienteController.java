package com.example.gomesrodris.archburgers.controller;

import com.example.gomesrodris.archburgers.domain.datagateway.ClienteGateway;
import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.usecases.ClienteUseCases;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;

import java.util.List;

public class ClienteController {
    private final ClienteUseCases clienteUseCases;

    public ClienteController(ClienteGateway clienteGateway) {
        clienteUseCases = new ClienteUseCases(clienteGateway);
    }

    public Cliente getClienteByCpf(Cpf cpf) {
        return clienteUseCases.getClienteByCpf(cpf);
    }

    public List<Cliente> listTodosClientes() {
        return clienteUseCases.listTodosClientes();
    }
}
