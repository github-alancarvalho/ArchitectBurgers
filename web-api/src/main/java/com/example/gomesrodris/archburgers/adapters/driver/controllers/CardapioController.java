package com.example.gomesrodris.archburgers.adapters.driver.controllers;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.services.CardapioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardapioController {
    private final CardapioServices cardapioServices;

    @Autowired
    public CardapioController(CardapioServices cardapioServices) {
        this.cardapioServices = cardapioServices;
    }

    @GetMapping(path = "/cardapio")
    public List<ItemCardapio> listItems() {
        return cardapioServices.listarItensCardapio();
    }
}
