package com.example.gomesrodris.archburgers.domain.repositories;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;

import java.util.List;

public interface ItemCardapioRepository {
    ItemCardapio findById(int id);
    List<ItemCardapio> findAll();

    List<ItemPedido> findByCarrinho(int idCarrinho);
    List<ItemPedido> findByPedido(int idPedido);
}
