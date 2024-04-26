package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public record Pedido(
        @NotNull Integer id,

        @Nullable Cliente clienteIdentificado,
        @Nullable String nomeClienteNaoIdentificado,

        @NotNull List<ItemCardapio> itens,

        @Nullable String observacoes,

        @NotNull StatusPedido status,

        @NotNull LocalDateTime dataHoraPedido
) {

    public ValorMonetario getValorTotal() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
