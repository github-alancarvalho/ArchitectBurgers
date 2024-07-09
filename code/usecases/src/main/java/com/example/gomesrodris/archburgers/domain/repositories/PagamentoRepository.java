package com.example.gomesrodris.archburgers.domain.repositories;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;

public interface PagamentoRepository {
    Pagamento findPagamentoByPedido(Integer idPedido);
    Pagamento salvarPagamento(Pagamento pagamento);
    void updateStatus(Pagamento pagamento);
}
