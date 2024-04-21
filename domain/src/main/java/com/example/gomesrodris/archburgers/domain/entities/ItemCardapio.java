package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;

public record ItemCardapio(int id,
                           TipoItemCardapio tipo,
                           String nome,
                           String descricao,
                           ValorMonetario valor) {


}
