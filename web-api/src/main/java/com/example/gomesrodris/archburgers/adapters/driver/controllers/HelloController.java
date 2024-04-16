package com.example.gomesrodris.archburgers.adapters.driver.controllers;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Welcome to the ArchitectBurgers WebAPI";
    }

    @GetMapping("/serializationExample")
    public ItemCardapio serializeModelClassExample() {
        return new ItemCardapio(21, ItemCardapio.Tipo.LANCHE, "Hamburger Vegetariano",
                "Hamburger de ervilha com queijo vegano");
    }
}
