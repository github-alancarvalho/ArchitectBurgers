package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.jetbrains.annotations.NotNull;

public record ItemCardapio(@NotNull Integer id,
                           @NotNull TipoItemCardapio tipo,
                           @NotNull String nome,
                           @NotNull String descricao,
                           @NotNull ValorMonetario valor) {


}
