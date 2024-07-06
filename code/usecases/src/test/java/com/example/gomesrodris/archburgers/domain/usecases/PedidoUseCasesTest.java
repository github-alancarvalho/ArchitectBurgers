package com.example.gomesrodris.archburgers.domain.usecases;

import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.external.PainelPedidos;
import com.example.gomesrodris.archburgers.domain.repositories.CarrinhoRepository;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.repositories.PedidoRepository;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import com.example.gomesrodris.archburgers.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoUseCasesTest {
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private CarrinhoRepository carrinhoRepository;
    @Mock
    private ItemCardapioRepository itemCardapioRepository;
    @Mock
    private Clock clock;
    @Mock
    private PainelPedidos painelPedidos;

    private PedidoUseCases pedidoUseCases;

    @BeforeEach
    void setUp() {
        pedidoUseCases = new PedidoUseCases(
                pedidoRepository, carrinhoRepository, itemCardapioRepository, clock, painelPedidos);
    }

    @Disabled("Refactor in progress, uses unimplemented method")
    @Test
    void criarPedido_missingParam() {
        assertThrows(IllegalArgumentException.class, () -> pedidoUseCases.criarPedido(null));
        assertThrows(IllegalArgumentException.class, () -> pedidoUseCases.criarPedido(
                new PedidoUseCases.CriarPedidoParam(null, "DINHEIRO")));
        assertThrows(IllegalArgumentException.class, () -> pedidoUseCases.criarPedido(
                new PedidoUseCases.CriarPedidoParam(12, "")));
    }

    @Disabled("Refactor in progress, uses unimplemented method")
    @Test
    void criarPedido_invalidPagamento() {
        assertThat(assertThrows(IllegalArgumentException.class, () -> pedidoUseCases.criarPedido(
                new PedidoUseCases.CriarPedidoParam(12, "Cheque")))
        ).hasMessage("Forma de pagamento inválida: Cheque");
    }

    @Disabled("Refactor in progress, uses unimplemented method")
    @Test
    void criarPedido_ok() {
        when(carrinhoRepository.getCarrinho(12)).thenReturn(
                Carrinho.carrinhoSalvoClienteIdentificado(12, new IdCliente(25),
                        "Lanche sem cebola",
                        LocalDateTime.of(2024, 5, 18, 14, 0))
        );
        when(itemCardapioRepository.findByCarrinho(12)).thenReturn(List.of(
                new ItemPedido(1,
                        new ItemCardapio(1000, TipoItemCardapio.LANCHE, "Hamburger", "Hamburger", new ValorMonetario("25.90"))
                ),
                new ItemPedido(2,
                        new ItemCardapio(1001, TipoItemCardapio.BEBIDA, "Refrigerante", "Refrigerante", new ValorMonetario("5.00"))
                )
        ));
        when(clock.localDateTime()).thenReturn(dateTime);

        var expectedPedido = Pedido.novoPedido(new IdCliente(25), null, List.of(
                new ItemPedido(1,
                        new ItemCardapio(1000, TipoItemCardapio.LANCHE, "Hamburger", "Hamburger", new ValorMonetario("25.90"))
                ),
                new ItemPedido(2,
                        new ItemCardapio(1001, TipoItemCardapio.BEBIDA, "Refrigerante", "Refrigerante", new ValorMonetario("5.00"))
                )
        ), "Lanche sem cebola", FormaPagamento.DINHEIRO.id(), dateTime);

        when(pedidoRepository.savePedido(expectedPedido)).thenReturn(expectedPedido.withId(33));

        var result = pedidoUseCases.criarPedido(
                new PedidoUseCases.CriarPedidoParam(12, "DINHEIRO"));

        assertThat(result).isEqualTo(expectedPedido.withId(33));
    }

    @Test
    void validarPedido() {
        var pedido = Pedido.pedidoRecuperado(42, new IdCliente(25), null,
                List.of(), "Lanche sem cebola", StatusPedido.RECEBIDO,
                FormaPagamento.DINHEIRO.id(), dateTime);

        when(pedidoRepository.getPedido(42)).thenReturn(pedido);
        when(itemCardapioRepository.findByPedido(42)).thenReturn(List.of(
                new ItemPedido(1,
                        new ItemCardapio(1000, TipoItemCardapio.LANCHE, "Hamburger", "Hamburger", new ValorMonetario("25.90"))
                )
        ));

        var expectedNewPedido = Pedido.pedidoRecuperado(42, new IdCliente(25), null, List.of(),
                "Lanche sem cebola", StatusPedido.PREPARACAO,
                FormaPagamento.DINHEIRO.id(), dateTime);

        ///
        var newPedido = pedidoUseCases.validarPedido(42);

        assertThat(newPedido).isEqualTo(expectedNewPedido.withItens(List.of(
                new ItemPedido(1,
                        new ItemCardapio(1000, TipoItemCardapio.LANCHE, "Hamburger", "Hamburger", new ValorMonetario("25.90"))
                )
        )));

        verify(pedidoRepository).updateStatus(expectedNewPedido);
    }

    @Test
    void setPedidoPronto() {
        var pedido = Pedido.pedidoRecuperado(45, new IdCliente(25), null,
                List.of(), "Lanche sem cebola", StatusPedido.PREPARACAO,
                FormaPagamento.DINHEIRO.id(), dateTime);

        when(pedidoRepository.getPedido(45)).thenReturn(pedido);
        when(itemCardapioRepository.findByPedido(45)).thenReturn(List.of(
                new ItemPedido(1,
                        new ItemCardapio(1000, TipoItemCardapio.LANCHE, "Hamburger", "Hamburger", new ValorMonetario("25.90"))
                )
        ));

        var expectedNewPedido = Pedido.pedidoRecuperado(45, new IdCliente(25), null, List.of(), "Lanche sem cebola",
                StatusPedido.PRONTO,
                FormaPagamento.DINHEIRO.id(), dateTime);

        Pedido expectedNewPedidoWithItens = expectedNewPedido.withItens(List.of(
                new ItemPedido(1,
                        new ItemCardapio(1000, TipoItemCardapio.LANCHE, "Hamburger", "Hamburger", new ValorMonetario("25.90"))
                )
        ));

        ///
        var newPedido = pedidoUseCases.setPronto(45);

        assertThat(newPedido).isEqualTo(expectedNewPedidoWithItens);

        verify(pedidoRepository).updateStatus(expectedNewPedido);
        verify(painelPedidos).notificarPedidoPronto(expectedNewPedidoWithItens);
    }

    @Test
    void listarPedidos_byStatus() {
        when(pedidoRepository.listPedidos(List.of(StatusPedido.RECEBIDO), null)).thenReturn(List.of(
                Pedido.pedidoRecuperado(42, new IdCliente(25), null,
                        List.of(), "Lanche sem cebola", StatusPedido.RECEBIDO,
                        FormaPagamento.DINHEIRO.id(), dateTime),
                Pedido.pedidoRecuperado(43, null, "Cliente Maria",
                        List.of(), null, StatusPedido.RECEBIDO,
                        FormaPagamento.DINHEIRO.id(), dateTime)
        ));

        var result = pedidoUseCases.listarPedidosByStatus(StatusPedido.RECEBIDO);

        assertThat(result).containsExactly(
                Pedido.pedidoRecuperado(42, new IdCliente(25), null,
                        List.of(), "Lanche sem cebola", StatusPedido.RECEBIDO,
                        FormaPagamento.DINHEIRO.id(), dateTime),
                Pedido.pedidoRecuperado(43, null, "Cliente Maria",
                        List.of(), null, StatusPedido.RECEBIDO,
                        FormaPagamento.DINHEIRO.id(), dateTime)
        );
    }

    @Test
    void listarPedidos_comAtraso() {
        when(clock.localDateTime()).thenReturn(LocalDateTime.of(
                2024, 5, 18, 10, 40, 28
        ));

        var expectedLimitTime = LocalDateTime.of(
                2024, 5, 18, 10, 20, 28
        );

        when(pedidoRepository.listPedidos(List.of(StatusPedido.RECEBIDO, StatusPedido.PREPARACAO), expectedLimitTime)).thenReturn(List.of(

                Pedido.pedidoRecuperado(42, new IdCliente(25), null,
                        List.of(), "Lanche sem cebola", StatusPedido.RECEBIDO,
                        FormaPagamento.DINHEIRO.id(), dateTime),
                Pedido.pedidoRecuperado(43, null, "Cliente Maria",
                        List.of(), null, StatusPedido.PREPARACAO,
                        FormaPagamento.DINHEIRO.id(), dateTime)
        ));

        var result = pedidoUseCases.listarPedidosComAtraso();

        assertThat(result).containsExactly(
                Pedido.pedidoRecuperado(42, new IdCliente(25), null,
                        List.of(), "Lanche sem cebola", StatusPedido.RECEBIDO,
                        FormaPagamento.DINHEIRO.id(), dateTime),
                Pedido.pedidoRecuperado(43, null, "Cliente Maria",
                        List.of(), null, StatusPedido.PREPARACAO,
                        FormaPagamento.DINHEIRO.id(), dateTime)
        );
    }

    ///////////
    private final LocalDateTime dateTime = LocalDateTime.of(2024, 5, 18, 15, 30);
}
