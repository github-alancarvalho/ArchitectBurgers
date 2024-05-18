package com.example.gomesrodris.archburgers.domain.repositories;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;

public interface PedidoRepository {
    Pedido getPedido(int idPedido);
    Pedido savePedido(Pedido pedido);
}
