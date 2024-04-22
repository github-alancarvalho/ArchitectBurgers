package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.Cpf;
import org.jetbrains.annotations.NotNull;

public record Cliente(
        @NotNull Integer id,
        @NotNull String nome,
        @NotNull Cpf cpf,
        @NotNull String email
) {
}
