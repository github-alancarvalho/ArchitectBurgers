package com.example.gomesrodris.archburgers.adapters.dto;

import com.example.gomesrodris.archburgers.adapters.testUtils.TestLocale;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemCardapioDtoTest {

    @BeforeAll
    static void beforeAll() {
        TestLocale.setDefault();
    }

    @Test
    void fromEntity() {
        var dto = ItemCardapioDto.fromEntity(new ItemCardapio(123,
                TipoItemCardapio.LANCHE, "Cheeseburger", "Hambúrguer com queijo", new ValorMonetario("25.99")));

        assertThat(dto).isEqualTo(new ItemCardapioDto(
                123, "LANCHE", "Cheeseburger", "Hambúrguer com queijo",
                new ValorMonetarioDto("25.99", "R$ 25,99")
        ));
    }

    @Test
    void toEntity() {
        var entity = new ItemCardapioDto(
                123, "LANCHE", "Cheeseburger", "Hambúrguer com queijo",
                new ValorMonetarioDto("25.99", null)
        ).toEntity();

        assertThat(entity).isEqualTo(new ItemCardapio(123,
                TipoItemCardapio.LANCHE, "Cheeseburger", "Hambúrguer com queijo", new ValorMonetario("25.99")));
    }

    @Test
    void toEntity_invalido() {
        assertThrows(IllegalArgumentException.class, () -> new ItemCardapioDto(
                123, "LANCHE", "Cheeseburger", null,
                new ValorMonetarioDto("25.99", null)
        ).toEntity());
    }
}