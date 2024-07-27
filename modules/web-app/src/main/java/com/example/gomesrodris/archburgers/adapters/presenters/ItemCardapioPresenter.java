package com.example.gomesrodris.archburgers.adapters.presenters;

import com.example.gomesrodris.archburgers.adapters.dto.ItemCardapioDto;
import com.example.gomesrodris.archburgers.adapters.dto.ValorMonetarioDto;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;

public class ItemCardapioPresenter {
    public static ItemCardapioDto entityToPresentationDto(ItemCardapio itemCardapio) {
        return new ItemCardapioDto(
                itemCardapio.id(),
                itemCardapio.tipo().name(),
                itemCardapio.nome(),
                itemCardapio.descricao(),
                ValorMonetarioDto.from(itemCardapio.valor())
        );
    }
}
