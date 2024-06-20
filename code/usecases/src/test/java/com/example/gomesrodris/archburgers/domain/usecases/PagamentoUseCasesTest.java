package com.example.gomesrodris.archburgers.domain.usecases;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.domain.entities.ConfirmacaoPagamento;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.exception.DomainArgumentException;
import com.example.gomesrodris.archburgers.domain.repositories.PagamentoRepository;
import com.example.gomesrodris.archburgers.domain.repositories.PedidoRepository;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import com.example.gomesrodris.archburgers.domain.valueobjects.FormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdCliente;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PagamentoUseCasesTest {
    private PagamentoRepository pagamentoRepository;
    private PedidoRepository pedidoRepository;
    private Clock clock;

    private PagamentoUseCases pagamentoUseCases;

    @BeforeEach
    void setUp() {
        pagamentoRepository = mock(PagamentoRepository.class);
        pedidoRepository = mock(PedidoRepository.class);
        clock = mock(Clock.class);

        pagamentoUseCases = new PagamentoUseCases(pagamentoRepository, pedidoRepository, clock);
    }

    @Test
    void confirmarPagamento_pedidoNotFound() {
        when(pedidoRepository.getPedido(33)).thenReturn(null);

        var e = assertThrows(DomainArgumentException.class,
                () -> pagamentoUseCases.confirmarPagamento(33, FormaPagamento.DINHEIRO, "-")
        );
        assertThat(e).hasMessageContaining("Pedido invalido: 33");
    }

    @Test
    void confirmarPagamento() {
        when(pedidoRepository.getPedido(33)).thenReturn(Pedido.pedidoRecuperado(33, new IdCliente(25), null,
                List.of(), "", StatusPedido.PAGAMENTO,
                FormaPagamento.DINHEIRO, null, dateTimePedido));

        when(pagamentoRepository.salvarConfirmacaoPagamento(new ConfirmacaoPagamento(null, FormaPagamento.DINHEIRO,
                "-", dateTimePagamento)))
                .thenReturn(new ConfirmacaoPagamento(12, FormaPagamento.DINHEIRO,
                        "-", dateTimePagamento));

        when(clock.localDateTime()).thenReturn(dateTimePagamento);

        //
        var result = pagamentoUseCases.confirmarPagamento(33, FormaPagamento.DINHEIRO, "-");

        var expectedPedidoFinal = Pedido.pedidoRecuperado(33, new IdCliente(25), null,
                List.of(), "", StatusPedido.RECEBIDO,
                FormaPagamento.DINHEIRO, 12, dateTimePedido);

        assertThat(result).isEqualTo(expectedPedidoFinal);
        verify(pedidoRepository).updateStatusEPagamento(expectedPedidoFinal);
    }

    private final LocalDateTime dateTimePedido = LocalDateTime.of(2024, 5, 18, 15, 30);
    private final LocalDateTime dateTimePagamento = LocalDateTime.of(2024, 5, 18, 15, 32);
}