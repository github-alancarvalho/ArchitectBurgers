package com.example.gomesrodris.archburgers.domain.repositories;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPedido;

import java.util.List;

public interface PedidoRepository {
    Pedido getPedido(int idPedido);
    Pedido savePedido(Pedido pedido);

    List<Pedido> listPedidos(StatusPedido filtroStatus);
}
