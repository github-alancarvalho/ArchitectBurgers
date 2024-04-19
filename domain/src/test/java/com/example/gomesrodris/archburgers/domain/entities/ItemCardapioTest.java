package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.DomainConstants;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.money.MonetaryAmount;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ItemCardapioTest {

    private ItemCardapio itemCardapio;

    @BeforeEach
    void setUp() {
        itemCardapio = new ItemCardapio(21, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                "Hamburger de ervilha com queijo vegano",
                Money.of(23.50, DomainConstants.CODIGO_MOEDA));
    }

    @Test
    void checkAttributes() {
        assertThat(itemCardapio.id()).isEqualTo(21);
        assertThat(itemCardapio.tipo()).isEqualTo(TipoItemCardapio.LANCHE);
        assertThat(itemCardapio.nome()).isEqualTo("Hamburger Vegetariano");
        assertThat(itemCardapio.descricao()).isEqualTo("Hamburger de ervilha com queijo vegano");
        assertThat(itemCardapio.valor()).isEqualTo(Money.of(new BigDecimal("23.50"), DomainConstants.CODIGO_MOEDA));
    }
}