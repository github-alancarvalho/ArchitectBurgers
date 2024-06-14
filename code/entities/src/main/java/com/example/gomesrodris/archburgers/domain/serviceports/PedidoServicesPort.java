package com.example.gomesrodris.archburgers.domain.serviceports;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.services.PedidoServices;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPedido;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PedidoServicesPort {
    Pedido criarPedido(PedidoServices.CriarPedidoParam param);

    List<Pedido> listarPedidosByStatus(@Nullable StatusPedido filtroStatus);

    List<Pedido> listarPedidosComAtraso();

    Pedido validarPedido(Integer idPedido);

    Pedido cancelarPedido(Integer idPedido);

    Pedido setPronto(Integer idPedido);

    Pedido finalizarPedido(Integer idPedido);

    record CriarPedidoParam(
            @Nullable Integer idCarrinho,
            @Nullable String formaPagamento
    ) {

    }
}
