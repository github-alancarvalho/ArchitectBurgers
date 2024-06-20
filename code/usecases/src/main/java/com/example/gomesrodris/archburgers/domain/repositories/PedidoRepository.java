package com.example.gomesrodris.archburgers.domain.repositories;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPedido;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository {
    Pedido getPedido(int idPedido);

    Pedido savePedido(Pedido pedido);

    List<Pedido> listPedidos(List<StatusPedido> filtroStatus,
                             @Nullable LocalDateTime olderThan);

    void updateStatusEPagamento(Pedido pedido);
}
