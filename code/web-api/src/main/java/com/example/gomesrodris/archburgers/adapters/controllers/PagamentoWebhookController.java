package com.example.gomesrodris.archburgers.adapters.controllers;

import com.example.gomesrodris.archburgers.adapters.dbgateways.TransactionManager;
import com.example.gomesrodris.archburgers.adapters.pagamento.MercadoPagoPaymentListener;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class PagamentoWebhookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PagamentoWebhookController.class);

    private final MercadoPagoPaymentListener mercadoPagoPaymentListener;
    private final TransactionManager transactionManager;

    @Autowired
    public PagamentoWebhookController(MercadoPagoPaymentListener mercadoPagoPaymentListener, TransactionManager transactionManager) {
        this.mercadoPagoPaymentListener = mercadoPagoPaymentListener;
        this.transactionManager = transactionManager;
    }

    @Operation(summary = "Grava confirmação de pagamento para o pedido, movendo o mesmo para status RECEBIDO")
    @PostMapping("/pagamento-webhook/mercado-pago")
    public String webHookMercadoPago(
            @RequestParam("id") String externalId, @RequestParam("data.id") String externalId2,
            @RequestHeader Map<String, String> headers,
            @RequestBody Map<String, Object> data) {

        try {
            transactionManager.runInTransaction(() -> {
                mercadoPagoPaymentListener.notificarUpdate(
                        externalId != null ? externalId : externalId2, // Em algumas chamadas o MP utiliza o param 1 e em outras o param 2...
                        headers, data
                );
                return null;
            });
        } catch (Exception e) {
            LOGGER.error("Error updating MercadoPago payment! {}", e.getMessage(), e);
            return "ERR";  // código de status HTTP é indiferente pois o provedor externo nao irá utilizar.
        }

        return "OK";
    }

}
