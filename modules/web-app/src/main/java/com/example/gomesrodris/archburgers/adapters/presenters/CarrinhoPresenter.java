package com.example.gomesrodris.archburgers.adapters.presenters;

import com.example.gomesrodris.archburgers.adapters.dto.CarrinhoDto;
import com.example.gomesrodris.archburgers.adapters.dto.ItemPedidoDto;
import com.example.gomesrodris.archburgers.adapters.dto.ValorMonetarioDto;
import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoPresenter {

    public static CarrinhoDto entityToPresentationDto(Carrinho carrinho) {
        List<ItemPedidoDto> dtoItens = new ArrayList<>();

        for (ItemPedido itemPedido : carrinho.itens()) {
            dtoItens.add(new ItemPedidoDto(itemPedido.numSequencia(), itemPedido.itemCardapio().id(),
                    itemPedido.itemCardapio().tipo().name(),
                    itemPedido.itemCardapio().nome(), itemPedido.itemCardapio().descricao(),
                    ValorMonetarioDto.from(itemPedido.itemCardapio().valor())));
        }

        return new CarrinhoDto(carrinho.id(),
                carrinho.idClienteIdentificado() != null ? carrinho.idClienteIdentificado().id() : null,
                carrinho.nomeClienteNaoIdentificado(),
                dtoItens, carrinho.observacoes(),
                ValorMonetarioDto.from(carrinho.getValorTotal()),
                DateUtils.toTimestamp(carrinho.dataHoraCarrinhoCriado()));
    }
}
