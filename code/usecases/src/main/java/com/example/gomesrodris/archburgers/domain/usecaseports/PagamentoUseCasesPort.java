package com.example.gomesrodris.archburgers.domain.usecaseports;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;

import java.util.List;

public interface PagamentoUseCasesPort {
    IdFormaPagamento validarFormaPagamento(String idFormaPagamento);

    Pagamento iniciarPagamento(Pedido pedido);

    /**
     * @param idPedido
     * @param idPedidoSistemaExterno Opcional, caso a forma de pagamento utilizada forneça esta informação no final
     */
    Pedido finalizarPagamento(int idPedido, String idPedidoSistemaExterno);

    List<DescricaoFormaPagamento> listarFormasPagamento();

    Pagamento consultarPagamento(int idPedido);

    record DescricaoFormaPagamento(IdFormaPagamento id, String descricao) {
    }
}
