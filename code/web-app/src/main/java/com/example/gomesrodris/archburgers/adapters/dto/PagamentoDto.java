package com.example.gomesrodris.archburgers.adapters.dto;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.utils.DateUtils;

public record PagamentoDto(
        Integer id,
        Integer idPedido,
        String formaPagamento,
        String status,
        ValorMonetarioDto valor,
        long dataHoraCriacao,
        long dataHoraAtualizacao,
        String codigoPagamentoCliente,
        String idPedidoSistemaExterno
) {

    public static PagamentoDto fromEntity(Pagamento pagamento) {
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
