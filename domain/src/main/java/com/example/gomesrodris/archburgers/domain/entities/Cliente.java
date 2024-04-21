package com.example.gomesrodris.archburgers.domain.entities;

public record Cliente(
        int id,
        String nome,
        String cpf,
        String email
) {
}
