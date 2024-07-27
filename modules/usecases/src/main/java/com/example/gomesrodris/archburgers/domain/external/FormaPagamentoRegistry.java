package com.example.gomesrodris.archburgers.domain.external;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.exception.DomainArgumentException;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Localização de serviços de implementação de formas de pagamento.
 */
public class FormaPagamentoRegistry {
    private final Map<IdFormaPagamento, FormaPagamento> formasPagamento;

    /**
     * No construtor são registrados todos os serviços adicionais a serem disponibilizados.
     * Os serviços internos conhecidos pelo core (DINHEIRO e CARTÃO MÁQUINA) são incluídos por padrão.
     */
    public FormaPagamentoRegistry(@NotNull List<FormaPagamento> formasPagamentoAdicionais) {
        var allFormas = new ArrayList<FormaPagamento>();
        allFormas.add(new FormaPagamentoInterna(IdFormaPagamento.DINHEIRO,
                "Pagamento em dinheiro direto ao caixa"));
        allFormas.add(new FormaPagamentoInterna(IdFormaPagamento.CARTAO_MAQUINA,
                "Pagamento na máquina da loja"));

        allFormas.addAll(formasPagamentoAdicionais);

        formasPagamento = allFormas.stream().collect(Collectors.toMap(
                FormaPagamento::id,
                f -> f
        ));
    }

    public FormaPagamento getFormaPagamento(@NotNull IdFormaPagamento id) throws DomainArgumentException {
        Objects.requireNonNull(id, "IdFormaPagamento nao pode ser nula");

        var forma = formasPagamento.get(id);

        if (forma == null)
            throw new DomainArgumentException("Forma de pagamento desconhecida: " + id.codigo());

        return forma;
    }

    public Collection<FormaPagamento> listAll() {
        return Collections.unmodifiableCollection(formasPagamento.values());
    }

    private record FormaPagamentoInterna(IdFormaPagamento id, String descricao) implements FormaPagamento {
        @Override
            public boolean isIntegracaoExterna() {
                return false;
            }

            @Override
            public InfoPagamentoExterno iniciarRegistroPagamento(Pedido pedido) {
                throw new UnsupportedOperationException("Forma de Pagamento interna");
            }
        }
}
