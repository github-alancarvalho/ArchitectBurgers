package com.example.gomesrodris.archburgers.domain.repositories;

import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ClienteRepository {
    @Nullable
    Cliente getClienteByCpf(@NotNull Cpf cpf);

    Cliente getClienteById(int id);

    Cliente salvarCliente(@NotNull Cliente cliente);

    List<Cliente> listarTodosClientes();
}
