package com.example.gomesrodris.archburgers.domain.entities;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.domain.valueobjects.FormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PedidoTest {

    private List<ItemPedido> sampleItens = List.of(
            new ItemPedido(1, new ItemCardapio(9, TipoItemCardapio.LANCHE,
                    "Cheeseburger", "Hamburger com queijo", new ValorMonetario("19.90")))
    );

    @Test
    void validar() {
        var p = Pedido.pedidoRecuperado(123, null, "Cliente José",
                sampleItens, null, StatusPedido.RECEBIDO,
                FormaPagamento.DINHEIRO, 444, LocalDateTime.now());

        var newP = p.validar();

        assertThat(newP.status()).isEqualTo(StatusPedido.PREPARACAO);
    }

    @Test
    void validar_statusInvalido() {
        var p = Pedido.pedidoRecuperado(123, null, "Cliente José",
                sampleItens, null, StatusPedido.PRONTO,
                FormaPagamento.DINHEIRO, 444, LocalDateTime.now());

        assertThat(
                assertThrows(IllegalArgumentException.class, p::validar)
        ).hasMessage("Status invalido para validação do pedido: PRONTO");
    }

    @Test
    void cancelar() {
        var p = Pedido.pedidoRecuperado(123, null, "Cliente José",
                sampleItens, null, StatusPedido.RECEBIDO,
                FormaPagamento.DINHEIRO, 444, LocalDateTime.now());

        var newP = p.cancelar();

        assertThat(newP.status()).isEqualTo(StatusPedido.CANCELADO);
    }

    @Test
    void setPronto() {
        var p = Pedido.pedidoRecuperado(123, null, "Cliente José",
                sampleItens, null, StatusPedido.PREPARACAO,
                FormaPagamento.DINHEIRO, 444, LocalDateTime.now());

        var newP = p.setPronto();

        assertThat(newP.status()).isEqualTo(StatusPedido.PRONTO);
    }
}