package com.example.gomesrodris.archburgers.domain.repositories;

import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdCliente;

public interface CarrinhoRepository {
    /**
     * Busca um Carrinho existente que esteja associado ao ID de cliente fornecido.
     * Retorna null caso não exista tal registro.
     */
    Carrinho getCarrinhoSalvoByCliente(IdCliente idCliente);

    Carrinho getCarrinho(int idCarrinho);

    Carrinho salvarCarrinhoVazio(Carrinho carrinho);

    void salvarItemCarrinho(Carrinho carrinho, ItemPedido newItem);

    void deleteCarrinho(Carrinho carrinho);

    void updateObservacaoCarrinho(Carrinho carrinho);
}
