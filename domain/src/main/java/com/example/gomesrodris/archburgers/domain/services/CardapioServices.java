package com.example.gomesrodris.archburgers.domain.services;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.serviceports.CardapioServicesPort;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CardapioServices implements CardapioServicesPort {
    private final ItemCardapioRepository itemCardapioRepository;

    public CardapioServices(ItemCardapioRepository itemCardapioRepository) {
        this.itemCardapioRepository = itemCardapioRepository;
    }

    @Override
    public List<ItemCardapio> listarItensCardapio(@Nullable TipoItemCardapio filtroTipo) {
        if (filtroTipo == null)
            return itemCardapioRepository.findAll();
        else
            return itemCardapioRepository.findByTipo(filtroTipo);
    }

    @Override
    public ItemCardapio salvarItemCardapio(@NotNull ItemCardapio itemCardapio) {
        if (itemCardapio.id() == null) {
            return itemCardapioRepository.salvarNovo(itemCardapio);
        } else {
            itemCardapioRepository.atualizar(itemCardapio);
            return itemCardapio;
        }
    }

    @Override
    public void excluirItemCardapio(int idItemCardapio) {
        itemCardapioRepository.excluir(idItemCardapio);
    }

}
