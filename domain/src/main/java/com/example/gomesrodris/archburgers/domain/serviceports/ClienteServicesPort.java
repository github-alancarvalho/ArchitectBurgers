package com.example.gomesrodris.archburgers.domain.serviceports;

import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;

import java.util.List;

public interface ClienteServicesPort {

    Cliente getClienteByCpf(Cpf cpf);

    List<Cliente> listTodosClientes();
}
