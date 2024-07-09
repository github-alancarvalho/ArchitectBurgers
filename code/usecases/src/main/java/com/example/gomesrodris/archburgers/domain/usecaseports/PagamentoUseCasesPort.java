package com.example.gomesrodris.archburgers.domain.usecaseports;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;

import java.util.List;

public interface PagamentoUseCasesPort {
    IdFormaPagamento validarFormaPagamento(String idFormaPagamento);

    Pagamento iniciarPagamento(Pedido pedido);

    Pedido finalizarPagamento(int idPedido);

    List<DescricaoFormaPagamento> listarFormasPagamento();

    record DescricaoFormaPagamento(IdFormaPagamento id, String descricao) {
    }
}
