package com.example.gomesrodris.archburgers.adapters.dto;

import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public record CarrinhoDto(
        Integer id,
        Integer idClienteIdentificado,
        String nomeClienteNaoIdentificado,
        List<Item> itens,
        String observacoes,
        ValorMonetarioDto valorTotal,
        Long dataHoraCarrinhoCriado
) {

    public static CarrinhoDto fromEntity(Carrinho carrinho) {
        List<Item> dtoItens = new ArrayList<>();

        for (int i = 0; i < carrinho.itens().size(); i++) {
            var nextItem = carrinho.itens().get(i);
            dtoItens.add(new Item(i, nextItem.id(), nextItem.tipo().name(),
                    nextItem.nome(), nextItem.descricao(), ValorMonetarioDto.from(nextItem.valor())));
        }

        return new CarrinhoDto(carrinho.id(),
                carrinho.idClienteIdentificado() != null ? carrinho.idClienteIdentificado().id() : null,
                carrinho.nomeClienteNaoIdentificado(),
                dtoItens, carrinho.observacoes(),
                ValorMonetarioDto.from(carrinho.getValorTotal()),
                DateUtils.toTimestamp(carrinho.dataHoraCarrinhoCriado()));
    }

    public record Item(
            int index,
            Integer idItemCardapio,
            String tipo,
            String nome,
            String descricao,
            ValorMonetarioDto valor
    ) {

    }
}
