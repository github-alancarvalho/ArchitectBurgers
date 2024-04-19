package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;

import javax.money.MonetaryAmount;

public record ItemCardapio(int id,
                           TipoItemCardapio tipo,
                           String nome,
                           String descricao,
                           MonetaryAmount valor) {


}
