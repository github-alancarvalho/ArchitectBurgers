package com.example.gomesrodris.archburgers.domain.usecaseports;

import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;

import java.util.List;

public interface ClienteUseCasesPort {

    Cliente getClienteByCpf(Cpf cpf);

    List<Cliente> listTodosClientes();
}
