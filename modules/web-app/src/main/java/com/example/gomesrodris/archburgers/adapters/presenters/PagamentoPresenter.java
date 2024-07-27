package com.example.gomesrodris.archburgers.adapters.presenters;

import com.example.gomesrodris.archburgers.adapters.dto.PagamentoDto;
import com.example.gomesrodris.archburgers.adapters.dto.ValorMonetarioDto;
import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.utils.DateUtils;

public class PagamentoPresenter {

    public static PagamentoDto entityToPresentationDto(Pagamento pagamento) {
        return new PagamentoDto(
                pagamento.id(),
                pagamento.idPedido(),
                pagamento.formaPagamento().codigo(),
                pagamento.status().name(),
                ValorMonetarioDto.from(pagamento.valor()),
                DateUtils.toTimestamp(pagamento.dataHoraCriacao()),
                DateUtils.toTimestamp(pagamento.dataHoraAtualizacao()),
                pagamento.codigoPagamentoCliente(),
                pagamento.idPedidoSistemaExterno()
        );
    }
}
