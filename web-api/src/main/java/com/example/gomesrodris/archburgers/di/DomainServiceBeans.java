package com.example.gomesrodris.archburgers.di;

import com.example.gomesrodris.archburgers.domain.notifications.PainelPedidos;
import com.example.gomesrodris.archburgers.domain.repositories.CarrinhoRepository;
import com.example.gomesrodris.archburgers.domain.repositories.ClienteRepository;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.repositories.PedidoRepository;
import com.example.gomesrodris.archburgers.domain.services.CardapioServices;
import com.example.gomesrodris.archburgers.domain.services.CarrinhoServices;
import com.example.gomesrodris.archburgers.domain.services.ClienteServices;
import com.example.gomesrodris.archburgers.domain.services.PedidoServices;
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
    public CardapioServices cardapioServices(ItemCardapioRepository itemCardapioRepository) {
        return new CardapioServices(itemCardapioRepository);
    }

    @Bean
    public ClienteServices clienteServices(ClienteRepository clienteRepository) {
        return new ClienteServices(clienteRepository);
    }

    @Bean
    public CarrinhoServices carrinhoServices(CarrinhoRepository carrinhoRepository,
                                             ClienteRepository clienteRepository,
                                             ItemCardapioRepository itemCardapioRepository,
                                             Clock clock) {
        return new CarrinhoServices(carrinhoRepository, clienteRepository, itemCardapioRepository, clock);
    }

    @Bean
    public PedidoServices pedidoServices(CarrinhoRepository carrinhoRepository,
                                         ItemCardapioRepository itemCardapioRepository,
                                         PedidoRepository pedidoRepository,
                                         Clock clock,
                                         PainelPedidos painelPedidos) {
        return new PedidoServices(pedidoRepository, carrinhoRepository, itemCardapioRepository, clock, painelPedidos);
    }
}
