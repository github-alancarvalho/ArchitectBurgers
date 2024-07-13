package com.example.gomesrodris.archburgers.domain.usecases;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.exception.DomainArgumentException;
import com.example.gomesrodris.archburgers.domain.external.FormaPagamento;
import com.example.gomesrodris.archburgers.domain.external.FormaPagamentoRegistry;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.repositories.PagamentoRepository;
import com.example.gomesrodris.archburgers.domain.repositories.PedidoRepository;
import com.example.gomesrodris.archburgers.domain.usecaseports.PagamentoUseCasesPort;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPagamento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class PagamentoUseCases implements PagamentoUseCasesPort {
    private final FormaPagamentoRegistry formaPagamentoRegistry;
    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemCardapioRepository itemCardapioRepository;
    private final Clock clock;

    public PagamentoUseCases(FormaPagamentoRegistry formaPagamentoRegistry,
                             PagamentoRepository pagamentoRepository,
                             PedidoRepository pedidoRepository,
                             ItemCardapioRepository itemCardapioRepository,
                             Clock clock) {
        this.formaPagamentoRegistry = formaPagamentoRegistry;
        this.pagamentoRepository = pagamentoRepository;
        this.pedidoRepository = pedidoRepository;
        this.itemCardapioRepository = itemCardapioRepository;
        this.clock = clock;
    }

    @Override
    public IdFormaPagamento validarFormaPagamento(String idFormaPagamento) throws DomainArgumentException {
        var formaPagamento = formaPagamentoRegistry.getFormaPagamento(new IdFormaPagamento(idFormaPagamento));
        return formaPagamento.id();
    }

    @Override
    public Pagamento iniciarPagamento(Pedido pedido) {
        var formaPagamento = formaPagamentoRegistry.getFormaPagamento(pedido.formaPagamento());

        FormaPagamento.InfoPagamentoExterno infoPagamentoExterno;
        if (formaPagamento.isIntegracaoExterna()) {
            infoPagamentoExterno = formaPagamento.iniciarRegistroPagamento(pedido);
        } else {
            infoPagamentoExterno = new FormaPagamento.InfoPagamentoExterno(null, null);
        }

        var pagamento = Pagamento.registroInicial(
                Objects.requireNonNull(pedido.id(), "Pedido deve estar gravado para iniciar pagamento"),
                formaPagamento.id(),
                ItemCardapio.somarValores(pedido.itens().stream().map(ItemPedido::itemCardapio).toList()),
                clock.localDateTime(),
                infoPagamentoExterno.codigoPagamentoCliente(),
                infoPagamentoExterno.idPedidoSistemaExterno());

        return pagamentoRepository.salvarPagamento(pagamento);
    }

    @Override
    public Pedido finalizarPagamento(int idPedido, String newIdPedidoSistemaExterno) {
        Pedido pedido = pedidoRepository.getPedido(idPedido);

        if (pedido == null) {
            throw new DomainArgumentException("Pedido invalido=" + idPedido);
        }

        Pagamento inicial = pagamentoRepository.findPagamentoByPedido(idPedido);

        if (inicial == null) {
            throw new DomainArgumentException("Pagamento nao encontrado. Pedido=" + idPedido);
        }

        pedido = pedido.withItens(itemCardapioRepository.findByPedido(idPedido));

        if (inicial.status() != StatusPagamento.PENDENTE) {
            throw new DomainArgumentException("Pagamento nao está Pendente. Pedido=" + idPedido);
        }

        LocalDateTime dataHoraPagamento = clock.localDateTime();

        Pagamento pagamentoFinalizado;
        if (newIdPedidoSistemaExterno != null && !newIdPedidoSistemaExterno.equals(inicial.idPedidoSistemaExterno())) {
            pagamentoFinalizado = inicial.finalizar(dataHoraPagamento, newIdPedidoSistemaExterno);
        } else {
            pagamentoFinalizado = inicial.finalizar(dataHoraPagamento);
        }

        var pedidoPago = pedido.confirmarPagamento(pagamentoFinalizado);

        pagamentoRepository.updateStatus(pagamentoFinalizado);

        pedidoRepository.updateStatus(pedidoPago);

        return pedidoPago;
    }

    @Override
    public List<DescricaoFormaPagamento> listarFormasPagamento() {
        return formaPagamentoRegistry.listAll()
                .stream().map(formaPagamento -> new DescricaoFormaPagamento(formaPagamento.id(), formaPagamento.descricao()))
                .toList();
    }

    @Override
    public Pagamento consultarPagamento(int idPedido) {
        return pagamentoRepository.findPagamentoByPedido(idPedido);
    }
}
