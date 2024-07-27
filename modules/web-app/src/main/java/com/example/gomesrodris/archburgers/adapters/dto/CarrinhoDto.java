package com.example.gomesrodris.archburgers.adapters.dto;

import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public record CarrinhoDto(
        Integer id,
        Integer idClienteIdentificado,
        String nomeClienteNaoIdentificado,
        List<ItemPedidoDto> itens,
        String observacoes,
        ValorMonetarioDto valorTotal,
        Long dataHoraCarrinhoCriado
) {

    public static CarrinhoDto fromEntity(Carrinho carrinho) {
        List<ItemPedidoDto> dtoItens = new ArrayList<>();

        for (ItemPedido itemPedido : carrinho.itens()) {
            dtoItens.add(new ItemPedidoDto(itemPedido.numSequencia(), itemPedido.itemCardapio().id(),
                    itemPedido.itemCardapio().tipo().name(),
                    itemPedido.itemCardapio().nome(), itemPedido.itemCardapio().descricao(),
                    ValorMonetarioDto.from(itemPedido.itemCardapio().valor())));
        }

        return new CarrinhoDto(carrinho.id(),
                carrinho.idClienteIdentificado() != null ? carrinho.idClienteIdentificado().id() : null,
                carrinho.nomeClienteNaoIdentificado(),
                dtoItens, carrinho.observacoes(),
                ValorMonetarioDto.from(carrinho.getValorTotal()),
                DateUtils.toTimestamp(carrinho.dataHoraCarrinhoCriado()));
    }

}
