package com.example.gomesrodris.archburgers.domain.entities;

/**
 * ItemCardapio acompanhado de um número de sequência, para ser associado a um carrinho/pedido
 * que apresenta itens ordenados
 */
public record ItemPedido(
        int numSequencia,
        ItemCardapio itemCardapio
) {
}
