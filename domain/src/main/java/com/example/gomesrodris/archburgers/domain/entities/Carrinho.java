package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public record Carrinho(
        @Nullable Integer id,
        @Nullable Cliente clienteIdentificado,
        @Nullable String nomeClienteNaoIdentificado,

        @NotNull List<ItemCardapio> itens,

        @Nullable String observacoes,

        @NotNull LocalDateTime dataHoraCarrinhoCriado
) {

    public ValorMonetario getValorTotal() {
        return ItemCardapio.somarValores(itens);
    }

    public Carrinho withId(Integer newId) {
        return new Carrinho(newId, clienteIdentificado, nomeClienteNaoIdentificado, itens, observacoes, dataHoraCarrinhoCriado);
    }
}
