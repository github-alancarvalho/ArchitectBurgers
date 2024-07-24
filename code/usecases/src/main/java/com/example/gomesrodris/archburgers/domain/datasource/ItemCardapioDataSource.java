package com.example.gomesrodris.archburgers.domain.datasource;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;

import java.util.List;

public interface ItemCardapioDataSource {
    ItemCardapio findById(int id);
    List<ItemCardapio> findAll();

    List<ItemPedido> findByCarrinho(int idCarrinho);
    List<ItemPedido> findByPedido(int idPedido);

    List<ItemCardapio> findByTipo(TipoItemCardapio filtroTipo);

    ItemCardapio salvarNovo(ItemCardapio itemCardapio);

    void atualizar(ItemCardapio itemCardapio);

    void excluir(int idItemCardapio);
}
