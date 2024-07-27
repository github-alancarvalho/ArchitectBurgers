package com.example.gomesrodris.archburgers.adapters.dto;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.utils.StringUtils;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.jetbrains.annotations.NotNull;

public record ItemCardapioDto(
        Integer id,
        String tipo,
        String nome,
        String descricao,
        ValorMonetarioDto valor
) {
    public @NotNull ItemCardapio toEntity() {
        if (StringUtils.isEmpty(tipo) || StringUtils.isEmpty(nome) || StringUtils.isEmpty(descricao)
                || valor == null || StringUtils.isEmpty(valor.raw())) {
            throw new IllegalArgumentException("Faltando valores obrigatórios: " + this);
        }

        return new ItemCardapio(
                id,
                TipoItemCardapio.valueOf(tipo),
                nome, descricao,
                new ValorMonetario(valor.raw())
        );
    }
}
