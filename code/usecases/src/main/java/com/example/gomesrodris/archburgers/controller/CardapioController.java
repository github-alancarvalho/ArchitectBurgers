package com.example.gomesrodris.archburgers.controller;

import com.example.gomesrodris.archburgers.domain.datagateway.ItemCardapioGateway;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.usecases.CardapioUseCases;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CardapioController {
    private final CardapioUseCases cardapioUseCases;

    public CardapioController(ItemCardapioGateway itemCardapioGateway) {
        cardapioUseCases = new CardapioUseCases(itemCardapioGateway);
    }

    public List<ItemCardapio> listarItensCardapio(@Nullable TipoItemCardapio filtroTipo) {
        return cardapioUseCases.listarItensCardapio(filtroTipo);
    }

    public ItemCardapio salvarItemCardapio(@NotNull ItemCardapio itemCardapio) {
        return cardapioUseCases.salvarItemCardapio(itemCardapio);
    }

    public void excluirItemCardapio(int idItemCardapio) {
        cardapioUseCases.excluirItemCardapio(idItemCardapio);
    }
}
