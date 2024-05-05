package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdCliente;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Cliente(
        @Nullable IdCliente id,
        @NotNull String nome,
        @NotNull Cpf cpf,
        @NotNull String email
) {

}
