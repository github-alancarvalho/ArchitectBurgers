package com.example.gomesrodris.archburgers.adapters.presenters;

import com.example.gomesrodris.archburgers.adapters.dto.ItemPedidoDto;
import com.example.gomesrodris.archburgers.adapters.dto.PedidoDto;
import com.example.gomesrodris.archburgers.adapters.dto.ValorMonetarioDto;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.utils.DateUtils;

import java.util.List;

public class PedidoPresenter {
    public static PedidoDto entityToPresentationDto(Pedido pedido) {
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
                pedido.formaPagamento().codigo(),
                ValorMonetarioDto.from(pedido.getValorTotal()),
                DateUtils.toTimestamp(pedido.dataHoraPedido()));
    }
}
