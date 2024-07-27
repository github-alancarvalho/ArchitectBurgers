package com.example.gomesrodris.archburgers.adapters.datagateway;

import com.example.gomesrodris.archburgers.domain.datagateway.ItemCardapioGateway;
import com.example.gomesrodris.archburgers.domain.datasource.ItemCardapioDataSource;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemCardapioGatewayImpl implements ItemCardapioGateway {
    private final ItemCardapioDataSource itemCardapioDataSource;

    public ItemCardapioGatewayImpl(ItemCardapioDataSource itemCardapioDataSource) {
        this.itemCardapioDataSource = itemCardapioDataSource;
    }

    @Override
    public ItemCardapio findById(int id) {
        return itemCardapioDataSource.findById(id);
    }

    @Override
    public List<ItemCardapio> findAll() {
        return itemCardapioDataSource.findAll();
    }

    @Override
    public List<ItemPedido> findByCarrinho(int idCarrinho) {
        return itemCardapioDataSource.findByCarrinho(idCarrinho);
    }

    @Override
    public List<ItemPedido> findByPedido(int idPedido) {
        return itemCardapioDataSource.findByPedido(idPedido);
    }

    @Override
    public List<ItemCardapio> findByTipo(TipoItemCardapio filtroTipo) {
        return itemCardapioDataSource.findByTipo(filtroTipo);
    }

    @Override
    public ItemCardapio salvarNovo(ItemCardapio itemCardapio) {
        return itemCardapioDataSource.salvarNovo(itemCardapio);
    }

    @Override
    public void atualizar(ItemCardapio itemCardapio) {
        itemCardapioDataSource.atualizar(itemCardapio);
    }

    @Override
    public void excluir(int idItemCardapio) {
        itemCardapioDataSource.excluir(idItemCardapio);
    }
}
