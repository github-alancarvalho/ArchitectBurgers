package com.example.gomesrodris.archburgers.adapters.dto;

import com.example.gomesrodris.archburgers.adapters.testUtils.TestLocale;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PedidoDtoTest {
    @BeforeAll
    static void beforeAll() {
        TestLocale.setDefault();
    }

    @Test
    void fromEntity() {
        var pedidoEntity = Pedido.pedidoRecuperado(13, null, "Wanderley", List.of(
                new ItemPedido(1,
                        new ItemCardapio(1000, TipoItemCardapio.LANCHE, "Hamburger", "Hamburger", new ValorMonetario("25.90"))
                ),
                new ItemPedido(2,
                        new ItemCardapio(1001, TipoItemCardapio.BEBIDA, "Refrigerante", "Refrigerante", new ValorMonetario("5.00"))
                )
        ), "Lanche sem cebola", StatusPedido.RECEBIDO,
                IdFormaPagamento.DINHEIRO, LocalDateTime.of(2024, 5, 18, 15, 30, 12));

        var dto = PedidoDto.fromEntity(pedidoEntity);

        assertThat(dto).isEqualTo(new PedidoDto(13, null, "Wanderley",
                List.of(
                        new ItemPedidoDto(1, 1000, "LANCHE", "Hamburger", "Hamburger", new ValorMonetarioDto("25.90", "R$ 25,90")),
                        new ItemPedidoDto(2, 1001, "BEBIDA", "Refrigerante", "Refrigerante", new ValorMonetarioDto("5.00", "R$ 5,00"))
                ), "Lanche sem cebola",
                "RECEBIDO", "DINHEIRO", new ValorMonetarioDto("30.90", "R$ 30,90"), 1716057012000L
        ));
    }
}