package com.example.gomesrodris.archburgers.adapters.dto;

import org.jetbrains.annotations.Nullable;

public record AddItemCarrinhoDto(
        @Nullable Integer idItemCardapio
) {

    public int getValidIdItemCardapio() {
        if (idItemCardapio == null)
            throw new IllegalArgumentException("idItemCardapio is required");
        return idItemCardapio;
    }
}
