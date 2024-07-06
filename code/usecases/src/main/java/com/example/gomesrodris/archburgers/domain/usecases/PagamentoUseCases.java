package com.example.gomesrodris.archburgers.domain.usecases;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.repositories.PagamentoRepository;
import com.example.gomesrodris.archburgers.domain.repositories.PedidoRepository;
import com.example.gomesrodris.archburgers.domain.usecaseports.PagamentoUseCasesPort;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import com.example.gomesrodris.archburgers.domain.valueobjects.FormaPagamento;

public class PagamentoUseCases implements PagamentoUseCasesPort {
    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;
    private final Clock clock;

    public PagamentoUseCases(PagamentoRepository pagamentoRepository,
                             PedidoRepository pedidoRepository,
                             Clock clock) {
        this.pagamentoRepository = pagamentoRepository;
        this.pedidoRepository = pedidoRepository;
        this.clock = clock;
    }

    @Override
    public Pedido confirmarPagamento(int idPedido,
                                     FormaPagamento formaPagamento,
                                     String infoAdicional) {
        throw new UnsupportedOperationException("Refactor in progress");

//        Pedido pedido = pedidoRepository.getPedido(idPedido);
//
//        if (pedido == null) {
//            throw new DomainArgumentException("Pedido invalido: " + idPedido);
//        }
//
//        LocalDateTime dataHoraPagamento = clock.localDateTime();
//
//        ConfirmacaoPagamento confirmacaoPagamento = new ConfirmacaoPagamento(
//                null, formaPagamento, infoAdicional, dataHoraPagamento
//        );
//        var confirmacaoSaved = pagamentoRepository.salvarConfirmacaoPagamento(confirmacaoPagamento);
//
//        var updated = pedido.confirmarPagamento(confirmacaoSaved);
//
//        pedidoRepository.updateStatusEPagamento(updated);
//
//        return updated;
    }
}
