package com.example.gomesrodris.archburgers.domain.entities;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.domain.valueobjects.FormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.InfoPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPedido;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PedidoTest {

    @Test
    void validar() {
        var p = new Pedido(123, null, "Cliente José",
                Collections.emptyList(), null, StatusPedido.RECEBIDO,
                new InfoPagamento(FormaPagamento.DINHEIRO), LocalDateTime.now());

        var newP = p.validar();

        assertThat(newP.status()).isEqualTo(StatusPedido.PREPARACAO);
    }

    @Test
    void validar_statusInvalido() {
        var p = new Pedido(123, null, "Cliente José",
                Collections.emptyList(), null, StatusPedido.PRONTO,
                new InfoPagamento(FormaPagamento.DINHEIRO), LocalDateTime.now());

        assertThat(
                assertThrows(IllegalArgumentException.class, p::validar)
        ).hasMessage("Status invalido para validação do pedido: PRONTO");
    }

    @Test
    void cancelar() {
        var p = new Pedido(123, null, "Cliente José",
                Collections.emptyList(), null, StatusPedido.RECEBIDO,
                new InfoPagamento(FormaPagamento.DINHEIRO), LocalDateTime.now());

        var newP = p.cancelar();

        assertThat(newP.status()).isEqualTo(StatusPedido.CANCELADO);
    }

    @Test
    void setPronto() {
        var p = new Pedido(123, null, "Cliente José",
                Collections.emptyList(), null, StatusPedido.PREPARACAO,
                new InfoPagamento(FormaPagamento.DINHEIRO), LocalDateTime.now());

        var newP = p.setPronto();

        assertThat(newP.status()).isEqualTo(StatusPedido.PRONTO);
    }
}