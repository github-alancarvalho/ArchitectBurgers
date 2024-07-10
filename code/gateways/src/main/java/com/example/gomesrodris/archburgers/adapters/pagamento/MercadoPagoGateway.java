package com.example.gomesrodris.archburgers.adapters.pagamento;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.external.FormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;

public class MercadoPagoGateway implements FormaPagamento {
    @Override
    public IdFormaPagamento id() {
        return new IdFormaPagamento("MercadoPago");
    }

    @Override
    public String descricao() {
        return "Pagamento pelo QrCode do aplicativo Mercado Pago";
    }

    @Override
    public boolean isIntegracaoExterna() {
        return true;
    }

    @Override
    public InfoPagamentoExterno iniciarRegistroPagamento(Pedido pedido) {
        return null;
    }
}
