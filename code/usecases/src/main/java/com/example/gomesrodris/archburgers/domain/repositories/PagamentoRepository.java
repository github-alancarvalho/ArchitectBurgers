package com.example.gomesrodris.archburgers.domain.repositories;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;

public interface PagamentoRepository {
    Pagamento salvarPagamento(Pagamento confirmacaoPagamento);
}
