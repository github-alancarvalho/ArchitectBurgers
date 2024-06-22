package com.example.gomesrodris.archburgers.domain.usecaseports;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.usecases.PedidoUseCases;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPedido;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PedidoUseCasesPort {
    Pedido criarPedido(PedidoUseCases.CriarPedidoParam param);

    List<Pedido> listarPedidosByStatus(@Nullable StatusPedido filtroStatus);

    List<Pedido> listarPedidosComAtraso();

    /**
     * Lista com todos os status exceto Finalizado e Cancelado
     */
    List<Pedido> listarPedidosAtivos();

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
