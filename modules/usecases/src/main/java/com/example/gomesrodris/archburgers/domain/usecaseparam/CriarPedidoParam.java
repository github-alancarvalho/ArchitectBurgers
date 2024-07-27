package com.example.gomesrodris.archburgers.domain.usecaseparam;

import org.jetbrains.annotations.Nullable;

public record CriarPedidoParam(
        @Nullable Integer idCarrinho,
        @Nullable String formaPagamento
) {

}
