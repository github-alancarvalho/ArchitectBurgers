package com.example.gomesrodris.archburgers.adapters.dto;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.adapters.testUtils.TestLocale;
import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdCliente;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CarrinhoDtoTest {

    @BeforeAll
    static void beforeAll() {
        TestLocale.setDefault();
    }

    @Test
    void fromEntity() {
        var entity1 = Carrinho.carrinhoSalvoClienteIdentificado(123, new IdCliente(98),
                        "Não adicionar molho", LocalDateTime.of(2024, 4, 29, 15, 30))
                .withItens(List.of(
                        new ItemPedido(1, new ItemCardapio(2, TipoItemCardapio.LANCHE, "Cheese Burger",
                                "Hamburger com queijo", new ValorMonetario("18.50"))
                        ),
                        new ItemPedido(2, new ItemCardapio(14, TipoItemCardapio.BEBIDA, "Refrigerante P",
                                "Refrigerante 300ml", new ValorMonetario("4.99"))
                        )
                ));

        var dto = CarrinhoDto.fromEntity(entity1);

        List<CarrinhoDto.Item> dtoItens = List.of(
                new CarrinhoDto.Item(1, 2, "LANCHE", "Cheese Burger",
                        "Hamburger com queijo", new ValorMonetarioDto("18.50", "R$ 18,50")),
                new CarrinhoDto.Item(2, 14, "BEBIDA", "Refrigerante P",
                        "Refrigerante 300ml", new ValorMonetarioDto("4.99", "R$ 4,99"))
        );

        assertThat(dto).isEqualTo(new CarrinhoDto(123, 98, null,
                dtoItens, "Não adicionar molho",
                new ValorMonetarioDto("23.49", "R$ 23,49"), 1714415400000L));
    }
}