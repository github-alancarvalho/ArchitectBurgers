package com.example.gomesrodris.archburgers.domain.valueobjects;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;

public record Combo(
        ItemCardapio lanche,
        ItemCardapio acompanhamento,
        ItemCardapio bebida,
        ItemCardapio sobremesa
) {
}
