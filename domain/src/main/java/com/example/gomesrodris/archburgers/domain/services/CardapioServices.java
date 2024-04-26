package com.example.gomesrodris.archburgers.domain.services;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardapioServices {
    private final ItemCardapioRepository itemCardapioRepository;

    @Autowired
    public CardapioServices(ItemCardapioRepository itemCardapioRepository) {
        this.itemCardapioRepository = itemCardapioRepository;
    }

    public List<ItemCardapio> listarItensCardapio() {
        return itemCardapioRepository.findAll();
    }

}
