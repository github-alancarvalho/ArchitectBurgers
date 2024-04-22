package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.Combo;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record Pedido(
        @NotNull Integer id,

        @Nullable Cliente clienteIdentificado,
        @Nullable String nomeClienteNaoIdentificado,

        @NotNull List<Combo> combos
) {

    public ValorMonetario getValorTotal() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
