package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ItemCardapio(@Nullable Integer id,
                           @NotNull TipoItemCardapio tipo,
                           @NotNull String nome,
                           @NotNull String descricao,
                           @NotNull ValorMonetario valor) {

    public ItemCardapio withId(Integer newId) {
        return new ItemCardapio(newId, tipo, nome, descricao, valor);
    }

    public static ValorMonetario somarValores(List<ItemCardapio> itens) {
        if (itens == null || itens.isEmpty()) {
            return ValorMonetario.ZERO;
        }

        var soma = ValorMonetario.ZERO;
        for (ItemCardapio item : itens) {
            soma = soma.somar(item.valor);
        }
        return soma;
    }
}
