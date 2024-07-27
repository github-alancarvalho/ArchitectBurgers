package com.example.gomesrodris.archburgers.adapters.pagamento;

import com.example.gomesrodris.archburgers.controller.PagamentoController;
import com.example.gomesrodris.archburgers.domain.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MercadoPagoPaymentListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MercadoPagoPaymentListener.class);

    private final MercadoPagoApi mercadoPagoApi;
    private final PagamentoController pagamentoController;

    public MercadoPagoPaymentListener(MercadoPagoApi mercadoPagoApi, PagamentoController pagamentoController){
        this.mercadoPagoApi = mercadoPagoApi;
        this.pagamentoController = pagamentoController;
    }

    public void notificarUpdate(String urlId, Map<String, String> headers, Map<String, Object> data) {
        var updatedOrder = mercadoPagoApi.getOrder((String) data.get("resource"), urlId);

        String paymentStatus = (String) updatedOrder.get("order_status");

        if (!"paid".equals(paymentStatus)) {
            LOGGER.info("Recebida notificação de atualização mas pagamento ainda nao esta finalizado: {} - {}",
                    urlId, paymentStatus);
            return;
        }

        String referenceParam = (String) updatedOrder.get("external_reference");
        if (StringUtils.isEmpty(referenceParam)) {
            throw new IllegalStateException("Missing external_reference! " + urlId + " -- " + data);
        }

        int idPedido;
        try {
            idPedido = Integer.parseInt(referenceParam);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid external_reference! " + urlId + "-" + referenceParam + " -- " + data);
        }

        pagamentoController.finalizarPagamento(idPedido, urlId);

        /*
        Obs: Versão experimental do código não está realizando validação do header X-Signature para verificar autenticidade
             da chamada ao webhook.
             https://www.mercadopago.com.br/developers/pt/docs/your-integrations/notifications/webhooks
         */


    }
}
