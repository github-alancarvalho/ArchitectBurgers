package com.example.gomesrodris.archburgers.adapters.dto;

import com.example.gomesrodris.archburgers.domain.entities.Cliente;
import org.jetbrains.annotations.Nullable;

/**
 * Representação simplificada "crua" da entidade Cliente para tráfego nos serviços
 */
public record ClienteDto(
        @Nullable Integer id,
        @Nullable String nome,
        @Nullable String cpf,
        @Nullable String email
) {

    public static ClienteDto fromEntity(Cliente cliente) {
        return new ClienteDto(
                cliente.id(),
                cliente.nome(),
                cliente.cpf().cpfNum(),
                cliente.email()
        );
    }
}
