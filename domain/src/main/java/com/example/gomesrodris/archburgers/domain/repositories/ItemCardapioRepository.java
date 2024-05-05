package com.example.gomesrodris.archburgers.domain.repositories;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;

import java.util.List;

public interface ItemCardapioRepository {
    List<ItemCardapio> findAll();
    List<ItemCardapio> findByCarrinho(int idCarrinho);
}
