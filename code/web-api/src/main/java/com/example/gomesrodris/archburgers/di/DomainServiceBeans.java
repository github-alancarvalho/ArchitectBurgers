package com.example.gomesrodris.archburgers.di;

import com.example.gomesrodris.archburgers.domain.notifications.PainelPedidos;
import com.example.gomesrodris.archburgers.domain.repositories.CarrinhoRepository;
import com.example.gomesrodris.archburgers.domain.repositories.ClienteRepository;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.repositories.PedidoRepository;
import com.example.gomesrodris.archburgers.domain.usecaseports.CardapioUseCasesPort;
import com.example.gomesrodris.archburgers.domain.usecaseports.CarrinhoUseCasesPort;
import com.example.gomesrodris.archburgers.domain.usecaseports.ClienteUseCasesPort;
import com.example.gomesrodris.archburgers.domain.usecaseports.PedidoUseCasesPort;
import com.example.gomesrodris.archburgers.domain.usecases.CardapioUseCases;
import com.example.gomesrodris.archburgers.domain.usecases.CarrinhoUseCases;
import com.example.gomesrodris.archburgers.domain.usecases.ClienteUseCases;
import com.example.gomesrodris.archburgers.domain.usecases.PedidoUseCases;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceBeans {

    @Bean
    Clock clock() {
        return new Clock();
    }

    @Bean
    public CardapioUseCasesPort cardapioUseCases(ItemCardapioRepository itemCardapioRepository) {
        return new CardapioUseCases(itemCardapioRepository);
    }

    @Bean
    public ClienteUseCasesPort clienteUseCases(ClienteRepository clienteRepository) {
        return new ClienteUseCases(clienteRepository);
    }

    @Bean
    public CarrinhoUseCasesPort carrinhoUseCases(CarrinhoRepository carrinhoRepository,
                                                 ClienteRepository clienteRepository,
                                                 ItemCardapioRepository itemCardapioRepository,
                                                 Clock clock) {
        return new CarrinhoUseCases(carrinhoRepository, clienteRepository, itemCardapioRepository, clock);
    }

    @Bean
    public PedidoUseCasesPort pedidoUseCases(CarrinhoRepository carrinhoRepository,
                                             ItemCardapioRepository itemCardapioRepository,
                                             PedidoRepository pedidoRepository,
                                             Clock clock,
                                             PainelPedidos painelPedidos) {
        return new PedidoUseCases(pedidoRepository, carrinhoRepository, itemCardapioRepository, clock, painelPedidos);
    }
}
