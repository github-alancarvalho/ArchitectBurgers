package com.example.gomesrodris.archburgers.domain.external;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import org.jetbrains.annotations.Nullable;

/**
 * Interface que permite a inclusão de serviços de pagamento com integração a sistemas de terceiros
 */
public interface FormaPagamento {
    /**
     * Fornece o ID desta forma de pagamento utilizado no registro do pedido e identificação durante o processamento
     */
    IdFormaPagamento id();

    /**
     * Descrição da forma de pagamento para listagem no registro de formas disponíveis
     */
    String descricao();

    /**
     * Informa se esta forma de pagamento utiliza integração externa
     */
    boolean isIntegracaoExterna();

    /**
     * Se a forma de pagamento é baseada em integração externa, este metodo será chamado
     * para fazer o registro inicial do pagamento no provedor
     */
    InfoPagamentoExterno iniciarRegistroPagamento(Pedido pedido);

    record InfoPagamentoExterno(
            @Nullable String codigoPagamentoCliente,
            @Nullable String idPedidoSistemaExterno
    ) {

    }
}
