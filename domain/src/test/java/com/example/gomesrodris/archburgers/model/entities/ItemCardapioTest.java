package com.example.gomesrodris.archburgers.model.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemCardapioTest {

    private ItemCardapio itemCardapio;

    @BeforeEach
    void setUp() {
        itemCardapio = new ItemCardapio(21, ItemCardapio.Tipo.SANDUICHE, "Hamburger Vegetariano",
                "Hamburger de ervilha com queijo vegano");
    }

    @Test
    void checkAttributes() {
        assertThat(itemCardapio.id()).isEqualTo(21);
        assertThat(itemCardapio.tipo()).isEqualTo(ItemCardapio.Tipo.SANDUICHE);
        assertThat(itemCardapio.nome()).isEqualTo("Hamburger Vegetariano");
        assertThat(itemCardapio.descricao()).isEqualTo("Hamburger de ervilha com queijo vegano");
    }
}