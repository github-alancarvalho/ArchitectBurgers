package com.example.gomesrodris.archburgers.adapters.dto;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;

public record ItemCardapioDto(
        Integer id,
        String tipo,
        String nome,
        String descricao,
        ValorMonetarioDto valor
) {
    public static ItemCardapioDto fromEntity(ItemCardapio itemCardapio) {
        return new ItemCardapioDto(
                itemCardapio.id(),
                itemCardapio.tipo().name(),
                itemCardapio.nome(),
                itemCardapio.descricao(),
                ValorMonetarioDto.from(itemCardapio.valor())
        );
    }
}
