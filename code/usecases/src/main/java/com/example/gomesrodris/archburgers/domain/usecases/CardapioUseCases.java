package com.example.gomesrodris.archburgers.domain.usecases;

import com.example.gomesrodris.archburgers.domain.datagateway.ItemCardapioGateway;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CardapioUseCases {
    private final ItemCardapioGateway itemCardapioGateway;

    public CardapioUseCases(ItemCardapioGateway itemCardapioGateway) {
        this.itemCardapioGateway = itemCardapioGateway;
    }

    public List<ItemCardapio> listarItensCardapio(@Nullable TipoItemCardapio filtroTipo) {
        if (filtroTipo == null)
            return itemCardapioGateway.findAll();
        else
            return itemCardapioGateway.findByTipo(filtroTipo);
    }

    public ItemCardapio salvarItemCardapio(@NotNull ItemCardapio itemCardapio) {
        if (itemCardapio.id() == null) {
            return itemCardapioGateway.salvarNovo(itemCardapio);
        } else {
            itemCardapioGateway.atualizar(itemCardapio);
            return itemCardapio;
        }
    }

    public void excluirItemCardapio(int idItemCardapio) {
        itemCardapioGateway.excluir(idItemCardapio);
    }

}
