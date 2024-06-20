package com.example.gomesrodris.archburgers.domain.usecases;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.notifications.PainelPedidos;
import com.example.gomesrodris.archburgers.domain.repositories.CarrinhoRepository;
import com.example.gomesrodris.archburgers.domain.repositories.ItemCardapioRepository;
import com.example.gomesrodris.archburgers.domain.repositories.PedidoRepository;
import com.example.gomesrodris.archburgers.domain.usecaseports.PedidoUseCasesPort;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import com.example.gomesrodris.archburgers.domain.utils.StringUtils;
import com.example.gomesrodris.archburgers.domain.valueobjects.FormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPedido;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class PedidoUseCases implements PedidoUseCasesPort {
    private final PedidoRepository pedidoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final ItemCardapioRepository itemCardapioRepository;
    private final Clock clock;
    private final PainelPedidos painelPedidos;

    public PedidoUseCases(PedidoRepository pedidoRepository, CarrinhoRepository carrinhoRepository, ItemCardapioRepository itemCardapioRepository,
                          Clock clock, PainelPedidos painelPedidos) {
        this.pedidoRepository = pedidoRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.itemCardapioRepository = itemCardapioRepository;
        this.clock = clock;
        this.painelPedidos = painelPedidos;
    }

    @Override
    public Pedido criarPedido(CriarPedidoParam param) {
        if (param == null)
            throw new IllegalArgumentException("Parameter missing");
        if (param.idCarrinho() == null)
            throw new IllegalArgumentException("idCarrinho missing");
        if (StringUtils.isEmpty(param.formaPagamento()))
            throw new IllegalArgumentException("formaPagamento missing");

        var formaPagamento = FormaPagamento.fromName(param.formaPagamento());

        var carrinho = carrinhoRepository.getCarrinho(param.idCarrinho());
        if (carrinho == null) {
            throw new IllegalArgumentException("Invalid idCarrinho " + param.idCarrinho());
        }

        var itens = itemCardapioRepository.findByCarrinho(param.idCarrinho());

        var pedido = Pedido.novoPedido(carrinho.idClienteIdentificado(), carrinho.nomeClienteNaoIdentificado(),
                itens, carrinho.observacoes(),
                formaPagamento, clock.localDateTime());

        Pedido saved = pedidoRepository.savePedido(pedido);

        carrinhoRepository.deleteCarrinho(carrinho);

        return saved;
    }

    @Override
    public List<Pedido> listarPedidosByStatus(@Nullable StatusPedido filtroStatus) {
        if (filtroStatus == null) {
            throw new IllegalArgumentException("Obrigatório informar um filtro");
        }

        var pedidos = pedidoRepository.listPedidos(List.of(filtroStatus), null);
        return pedidos.stream().map(p -> {
            var itens = itemCardapioRepository.findByPedido(Objects.requireNonNull(p.id(), "Expected pedidos to have ID"));
            return p.withItens(itens);
        }).toList();
    }

    @Override
    public List<Pedido> listarPedidosComAtraso() {
        var now = clock.localDateTime();
        var olderThan = now.minusMinutes(20);

        var pedidos = pedidoRepository.listPedidos(List.of(StatusPedido.RECEBIDO, StatusPedido.PREPARACAO), olderThan);
        return pedidos.stream().map(p -> {
            var itens = itemCardapioRepository.findByPedido(Objects.requireNonNull(p.id(), "Expected pedidos to have ID"));
            return p.withItens(itens);
        }).toList();
    }

    @Override
    public Pedido validarPedido(Integer idPedido) {
        return loadAndApply(idPedido, Pedido::validar);
    }

    @Override
    public Pedido cancelarPedido(Integer idPedido) {
        return loadAndApply(idPedido, Pedido::cancelar);
    }

    @Override
    public Pedido setPronto(Integer idPedido) {
        Pedido updated = loadAndApply(idPedido, Pedido::setPronto);
        painelPedidos.notificarPedidoPronto(updated);
        return updated;
    }

    @Override
    public Pedido finalizarPedido(Integer idPedido) {
        return loadAndApply(idPedido, Pedido::finalizar);
    }

    private @NotNull Pedido loadAndApply(Integer idPedido, Function<Pedido, Pedido> update) {
        var pedido = pedidoRepository.getPedido(Objects.requireNonNull(idPedido, "ID não pode ser null"));

        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado: " + idPedido);
        }

        var atualizado = update.apply(pedido);
        pedidoRepository.updateStatusEPagamento(atualizado);

        var itens = itemCardapioRepository.findByPedido(idPedido);

        return atualizado.withItens(itens);
    }
}
