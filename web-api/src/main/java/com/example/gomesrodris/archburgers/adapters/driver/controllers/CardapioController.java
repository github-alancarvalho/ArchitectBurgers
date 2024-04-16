package com.example.gomesrodris.archburgers.adapters.driver.controllers;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.services.CardapioServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardapioController {
    private final CardapioServices cardapioServices;

    public CardapioController(CardapioServices cardapioServices) {
        this.cardapioServices = cardapioServices;
    }

    @GetMapping(path = "/cardapio/lanches")
    public List<ItemCardapio> listLanches() {
        return cardapioServices.listLanches();
    }
}
