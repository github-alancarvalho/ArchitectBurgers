package com.example.gomesrodris.archburgers.adapters.dto;

import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public record CarrinhoDto(
        Integer id,
        Integer idClienteIdentificado,
        String nomeClienteNaoIdentificado,
        List<ItemPedidoDto> itens,
        String observacoes,
        ValorMonetarioDto valorTotal,
        Long dataHoraCarrinhoCriado
) {


}
