package com.example.gomesrodris.archburgers.domain.repositories;

import com.example.gomesrodris.archburgers.domain.entities.ConfirmacaoPagamento;

public interface PagamentoRepository {
    ConfirmacaoPagamento salvarConfirmacaoPagamento(ConfirmacaoPagamento confirmacaoPagamento);
}
