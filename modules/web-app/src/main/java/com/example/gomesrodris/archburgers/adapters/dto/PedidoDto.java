package com.example.gomesrodris.archburgers.adapters.dto;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.utils.DateUtils;

import java.util.List;

public record PedidoDto(
        Integer id,
        Integer idClienteIdentificado,
        String nomeClienteNaoIdentificado,
        List<ItemPedidoDto> itens,
        String observacoes,
        String status,
        String formaPagamento,
        ValorMonetarioDto valorTotal,
        Long dataHoraCarrinhoCriado
) {

}
