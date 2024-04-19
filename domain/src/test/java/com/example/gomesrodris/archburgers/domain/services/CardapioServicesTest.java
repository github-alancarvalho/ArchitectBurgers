package com.example.gomesrodris.archburgers.domain.services;

import com.example.gomesrodris.archburgers.domain.DomainConstants;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardapioServicesTest {
    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    private CardapioServices cardapioServices;

    @BeforeEach
    void setUp() {
        cardapioServices = new CardapioServices(itemCardapioRepository);
    }

    @Test
    void listLanches() {
        when(itemCardapioRepository.findByTipo(TipoItemCardapio.LANCHE)).thenReturn(List.of(
                new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                        "Hamburger de ervilha com queijo prato",
                        Money.of(20.00, DomainConstants.CODIGO_MOEDA)),
                new ItemCardapio(2, TipoItemCardapio.LANCHE, "Veggie Cheddar",
                        "Hamburger do Futuro com cebolas caramelizadas e cheddar vegano",
                        Money.of(25.00, DomainConstants.CODIGO_MOEDA))
        ));

        var result = cardapioServices.listLanches();

        assertThat(result).containsExactly(
                new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                        "Hamburger de ervilha com queijo prato",
                        Money.of(20.00, DomainConstants.CODIGO_MOEDA)),
                new ItemCardapio(2, TipoItemCardapio.LANCHE, "Veggie Cheddar",
                        "Hamburger do Futuro com cebolas caramelizadas e cheddar vegano",
                        Money.of(25.00, DomainConstants.CODIGO_MOEDA))
        );
    }
}