package com.example.gomesrodris.archburgers.adapters.pagamento;

import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.external.FormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;

import java.util.UUID;

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
        return new InfoPagamentoExterno(
                UUID.randomUUID().toString(),
                "00020101021243650016COM.MERCADOLIBRE020130636787e9685-7de5-43f1-b09a-6d70f6f6c1e45204000053039865802BR5909Test Test6009SAO PAULO62070503***63043962"
        );

        /*
        TO-DO:

        Chamar endpoint de criação de pedido
        https://api.mercadopago.com/instore/orders/qr/seller/collectors/{{user_id}}/pos/{{pos_id}}/qrs

        Obter campo qrData do retorno:
            {
              "in_store_order_id": "787e9685-7de5-43f1-b09a-6d70f6f6c1e4",
              "qr_data": "00020101021243650016COM.MERCADOLIBRE020...O62070503***63043962"
            }
         */
    }
}
