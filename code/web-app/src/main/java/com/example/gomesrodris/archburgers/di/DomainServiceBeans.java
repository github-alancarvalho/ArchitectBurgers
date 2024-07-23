package com.example.gomesrodris.archburgers.di;

import com.example.gomesrodris.archburgers.adapters.pagamento.MercadoPagoApi;
import com.example.gomesrodris.archburgers.adapters.pagamento.MercadoPagoGateway;
import com.example.gomesrodris.archburgers.adapters.presenters.QrCodePresenter;
import com.example.gomesrodris.archburgers.domain.external.FormaPagamentoRegistry;
import com.example.gomesrodris.archburgers.domain.external.PainelPedidos;
import com.example.gomesrodris.archburgers.domain.repositories.*;
import com.example.gomesrodris.archburgers.domain.usecaseports.*;
import com.example.gomesrodris.archburgers.domain.usecases.*;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
    public FormaPagamentoRegistry formaPagamentoRegistry(MercadoPagoGateway mercadoPagoGateway) {
        return new FormaPagamentoRegistry(List.of(
                mercadoPagoGateway
        ));
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
                                             PagamentoUseCasesPort pagamentoUseCases,
                                             Clock clock,
                                             PainelPedidos painelPedidos) {
        return new PedidoUseCases(pedidoRepository, carrinhoRepository, itemCardapioRepository,
                pagamentoUseCases, clock, painelPedidos);
    }

    @Bean
    public PagamentoUseCasesPort pagamentoUseCases(FormaPagamentoRegistry formaPagamentoRegistry,
                                                   PagamentoRepository pagamentoRepository,
                                                   PedidoRepository pedidoRepository,
                                                   ItemCardapioRepository itemCardapioRepository,
                                                   Clock clock) {
        return new PagamentoUseCases(formaPagamentoRegistry, pagamentoRepository,
                pedidoRepository, itemCardapioRepository, clock);
    }

    @Bean
    public QrCodePresenter qrCodePresenter() {
        return new QrCodePresenter();
    }
}
