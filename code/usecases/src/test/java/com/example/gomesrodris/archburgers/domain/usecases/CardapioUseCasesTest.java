package com.example.gomesrodris.archburgers.domain.usecases;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardapioUseCasesTest {
    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    private CardapioUseCases cardapioUseCases;

    @BeforeEach
    void setUp() {
        cardapioUseCases = new CardapioUseCases(itemCardapioRepository);
    }

    @Test
    void listLanches() {
        when(itemCardapioRepository.findAll()).thenReturn(List.of(
                new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                        "Hamburger de ervilha com queijo prato",
                        new ValorMonetario("20.00")),
                new ItemCardapio(2, TipoItemCardapio.LANCHE, "Veggie Cheddar",
                        "Hamburger do Futuro com cebolas caramelizadas e cheddar vegano",
                        new ValorMonetario("25.00"))
        ));

        var result = cardapioUseCases.listarItensCardapio(null);

        assertThat(result).containsExactly(
                new ItemCardapio(1, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                        "Hamburger de ervilha com queijo prato",
                        new ValorMonetario("20.00")),
                new ItemCardapio(2, TipoItemCardapio.LANCHE, "Veggie Cheddar",
                        "Hamburger do Futuro com cebolas caramelizadas e cheddar vegano",
                        new ValorMonetario("25.00"))
        );
    }
}