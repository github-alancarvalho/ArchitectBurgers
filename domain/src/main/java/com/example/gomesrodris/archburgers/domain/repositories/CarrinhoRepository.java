package com.example.gomesrodris.archburgers.domain.repositories;

import com.example.gomesrodris.archburgers.domain.entities.Carrinho;

public interface CarrinhoRepository {
    /**
     * Busca um Carrinho existente que esteja associado ao ID de cliente fornecido.
     * Retorna null caso não exista tal registro.
     */
    Carrinho getCarrinhoByClienteId(int idCliente);

    Carrinho salvarCarrinho(Carrinho carrinho);
}
