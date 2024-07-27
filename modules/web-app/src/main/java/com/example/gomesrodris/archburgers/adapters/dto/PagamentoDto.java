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

}
