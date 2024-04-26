package com.example.gomesrodris.archburgers.domain.services;

import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.repositories.CarrinhoRepository;
import com.example.gomesrodris.archburgers.domain.repositories.ClienteRepository;

public class CarrinhoServices {

    private final CarrinhoRepository carrinhoRepository;
    private final ClienteRepository clienteRepository;

    public CarrinhoServices(CarrinhoRepository carrinhoRepository, ClienteRepository clienteRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.clienteRepository = clienteRepository;
    }

    public Carrinho criarCarrinhoClienteIdentificado(int idCliente) {
//        var carrinho = new Carrinho()
        return null;
    }
}
