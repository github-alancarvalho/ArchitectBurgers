package com.example.gomesrodris.archburgers.domain.services;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.domain.entities.Carrinho;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.repositories.CarrinhoRepository;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.repositories.PedidoRepository;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import com.example.gomesrodris.archburgers.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
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
class PedidoServicesTest {
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private CarrinhoRepository carrinhoRepository;
    @Mock
    private ItemCardapioRepository itemCardapioRepository;
    @Mock
    private Clock clock;

    private PedidoServices pedidoServices;

    @BeforeEach
    void setUp() {
        pedidoServices = new PedidoServices(
                pedidoRepository, carrinhoRepository, itemCardapioRepository, clock);
    }

    @Test
    void criarPedido_missingParam() {
        assertThrows(IllegalArgumentException.class, () -> pedidoServices.criarPedido(null));
        assertThrows(IllegalArgumentException.class, () -> pedidoServices.criarPedido(
                new PedidoServices.CriarPedidoParam(null, "DINHEIRO")));
        assertThrows(IllegalArgumentException.class, () -> pedidoServices.criarPedido(
                new PedidoServices.CriarPedidoParam(12, "")));
    }

    @Test
    void criarPedido_invalidPagamento() {
        assertThat(assertThrows(IllegalArgumentException.class, () -> pedidoServices.criarPedido(
                new PedidoServices.CriarPedidoParam(12, "Cheque")))
        ).hasMessage("Forma de pagamento inválida: Cheque");
    }

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

        var expectedPedido = new Pedido(null, new IdCliente(25), null, List.of(
                new ItemPedido(1,
                        new ItemCardapio(1000, TipoItemCardapio.LANCHE, "Hamburger", "Hamburger", new ValorMonetario("25.90"))
                ),
                new ItemPedido(2,
                        new ItemCardapio(1001, TipoItemCardapio.BEBIDA, "Refrigerante", "Refrigerante", new ValorMonetario("5.00"))
                )
        ), "Lanche sem cebola", StatusPedido.RECEBIDO,
                new InfoPagamento(FormaPagamento.DINHEIRO), dateTime);

        when(pedidoRepository.savePedido(expectedPedido)).thenReturn(expectedPedido.withId(33));

        var result = pedidoServices.criarPedido(
                new PedidoServices.CriarPedidoParam(12, "DINHEIRO"));

        assertThat(result).isEqualTo(expectedPedido.withId(33));
    }

    @Test
    void validarPedido() {
        var pedido = new Pedido(42, new IdCliente(25), null,
                List.of(), "Lanche sem cebola", StatusPedido.RECEBIDO,
                new InfoPagamento(FormaPagamento.DINHEIRO), dateTime);

        when(pedidoRepository.getPedido(42)).thenReturn(pedido);
        when(itemCardapioRepository.findByPedido(42)).thenReturn(List.of(
                new ItemPedido(1,
                        new ItemCardapio(1000, TipoItemCardapio.LANCHE, "Hamburger", "Hamburger", new ValorMonetario("25.90"))
                )
        ));

        var expectedNewPedido = new Pedido(42, new IdCliente(25), null, List.of(), "Lanche sem cebola", StatusPedido.PREPARACAO,
                new InfoPagamento(FormaPagamento.DINHEIRO), dateTime);

        ///
        var newPedido = pedidoServices.validarPedido(42);

        assertThat(newPedido).isEqualTo(expectedNewPedido.withItens(List.of(
                new ItemPedido(1,
                        new ItemCardapio(1000, TipoItemCardapio.LANCHE, "Hamburger", "Hamburger", new ValorMonetario("25.90"))
                )
        )));

        verify(pedidoRepository).updateStatus(expectedNewPedido);
    }

    @Test
    void listarPedidos_byStatus() {
        when(pedidoRepository.listPedidos(List.of(StatusPedido.RECEBIDO), null)).thenReturn(List.of(
                new Pedido(42, new IdCliente(25), null,
                        List.of(), "Lanche sem cebola", StatusPedido.RECEBIDO,
                        new InfoPagamento(FormaPagamento.DINHEIRO), dateTime),
                new Pedido(43, null, "Cliente Maria",
                        List.of(), null, StatusPedido.RECEBIDO,
                        new InfoPagamento(FormaPagamento.DINHEIRO), dateTime)
        ));

        var result = pedidoServices.listarPedidosByStatus(StatusPedido.RECEBIDO);

        assertThat(result).containsExactly(
                new Pedido(42, new IdCliente(25), null,
                        List.of(), "Lanche sem cebola", StatusPedido.RECEBIDO,
                        new InfoPagamento(FormaPagamento.DINHEIRO), dateTime),
                new Pedido(43, null, "Cliente Maria",
                        List.of(), null, StatusPedido.RECEBIDO,
                        new InfoPagamento(FormaPagamento.DINHEIRO), dateTime)
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

                new Pedido(42, new IdCliente(25), null,
                        List.of(), "Lanche sem cebola", StatusPedido.RECEBIDO,
                        new InfoPagamento(FormaPagamento.DINHEIRO), dateTime),
                new Pedido(43, null, "Cliente Maria",
                        List.of(), null, StatusPedido.PREPARACAO,
                        new InfoPagamento(FormaPagamento.DINHEIRO), dateTime)
        ));

        var result = pedidoServices.listarPedidosComAtraso();

        assertThat(result).containsExactly(
                new Pedido(42, new IdCliente(25), null,
                        List.of(), "Lanche sem cebola", StatusPedido.RECEBIDO,
                        new InfoPagamento(FormaPagamento.DINHEIRO), dateTime),
                new Pedido(43, null, "Cliente Maria",
                        List.of(), null, StatusPedido.PREPARACAO,
                        new InfoPagamento(FormaPagamento.DINHEIRO), dateTime)
        );
    }

    ///////////
    private final LocalDateTime dateTime = LocalDateTime.of(2024, 5, 18, 15, 30);
}
