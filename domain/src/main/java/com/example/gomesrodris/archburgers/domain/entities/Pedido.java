package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.IdCliente;
import com.example.gomesrodris.archburgers.domain.valueobjects.InfoPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public record Pedido(
        @Nullable Integer id,

        @Nullable IdCliente idClienteIdentificado,
        @Nullable String nomeClienteNaoIdentificado,

        @NotNull List<ItemPedido> itens,

        @Nullable String observacoes,

        @NotNull StatusPedido status,

        @NotNull InfoPagamento infoPagamento,

        @NotNull LocalDateTime dataHoraPedido
) {

    public ValorMonetario getValorTotal() {
        return ItemCardapio.somarValores(itens.stream().map(ItemPedido::itemCardapio).toList());
    }

    public Pedido withId(int newId) {
        return new Pedido(newId, idClienteIdentificado, nomeClienteNaoIdentificado, itens, observacoes, status, infoPagamento, dataHoraPedido);
    }

    public Pedido withItens(List<ItemPedido> newItens) {
        return new Pedido(id, idClienteIdentificado, nomeClienteNaoIdentificado, newItens, observacoes, status, infoPagamento, dataHoraPedido);
    }
}
