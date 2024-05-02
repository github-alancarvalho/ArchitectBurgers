package com.example.gomesrodris.archburgers.domain.services;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;

import java.util.List;

public class CardapioServices {
    private final ItemCardapioRepository itemCardapioRepository;

    public CardapioServices(ItemCardapioRepository itemCardapioRepository) {
        this.itemCardapioRepository = itemCardapioRepository;
    }

    public List<ItemCardapio> listarItensCardapio() {
        return itemCardapioRepository.findAll();
    }

}
