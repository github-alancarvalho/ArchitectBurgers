package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ItemCardapioTest {

    private ItemCardapio itemCardapio;

    @BeforeEach
    void setUp() {
        itemCardapio = new ItemCardapio(21, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                "Hamburger de ervilha com queijo vegano",
                new ValorMonetario("23.50"));
    }

    @Test
    void checkAttributes() {
        assertThat(itemCardapio.id()).isEqualTo(21);
        assertThat(itemCardapio.tipo()).isEqualTo(TipoItemCardapio.LANCHE);
        assertThat(itemCardapio.nome()).isEqualTo("Hamburger Vegetariano");
        assertThat(itemCardapio.descricao()).isEqualTo("Hamburger de ervilha com queijo vegano");
        assertThat(itemCardapio.valor()).isEqualTo(new ValorMonetario(new BigDecimal("23.50")));
    }
}