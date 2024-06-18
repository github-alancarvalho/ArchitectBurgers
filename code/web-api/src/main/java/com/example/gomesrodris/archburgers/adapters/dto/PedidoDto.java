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
        Integer idConfirmacaoPagamento,
        ValorMonetarioDto valorTotal,
        Long dataHoraCarrinhoCriado
) {
    public static PedidoDto fromEntity(Pedido pedido) {
        List<ItemPedidoDto> dtoItens = pedido.itens().stream().map(
                        itemPedido -> new ItemPedidoDto(itemPedido.numSequencia(),
                                itemPedido.itemCardapio().id(), itemPedido.itemCardapio().tipo().name(),
                                itemPedido.itemCardapio().nome(), itemPedido.itemCardapio().descricao(),
                                ValorMonetarioDto.from(itemPedido.itemCardapio().valor())))
                .toList();

        return new PedidoDto(pedido.id(),
                pedido.idClienteIdentificado() != null ? pedido.idClienteIdentificado().id() : null,
                pedido.nomeClienteNaoIdentificado(),
                dtoItens,
                pedido.observacoes(),
                pedido.status().name(),
                pedido.formaPagamento().name(),
                pedido.idConfirmacaoPagamento(),
                ValorMonetarioDto.from(pedido.getValorTotal()),
                DateUtils.toTimestamp(pedido.dataHoraPedido()));
    }
}
