package com.example.gomesrodris.archburgers.domain.usecaseports;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.FormaPagamento;

import java.time.LocalDateTime;

public interface PagamentoUseCasesPort {
    Pedido confirmarPagamento(int idPedido,
                              FormaPagamento formaPagamento,
                              String infoAdicional);
}
