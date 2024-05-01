package com.example.gomesrodris.archburgers.adapters.driven.infra;

import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.repositories.CarrinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CarrinhoRepositoryJdbcImpl implements CarrinhoRepository {

    private final DatabaseConnection databaseConnection;

    @Autowired
    public CarrinhoRepositoryJdbcImpl(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public Carrinho getCarrinhoByClienteId(int idCliente) {
        return null;
    }

    @Override
    public Carrinho salvarCarrinho(Carrinho carrinho) {
        return null;
    }
}
