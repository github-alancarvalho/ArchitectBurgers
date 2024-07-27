package com.example.gomesrodris.archburgers.adapters.pagamento;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.controller.PagamentoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MercadoPagoPaymentListenerTest {
    private MercadoPagoApi mercadoPagoApi;
    private PagamentoController pagamentoController;

    private MercadoPagoPaymentListener paymentListener;

    @BeforeEach
    void setUp() {
        mercadoPagoApi = mock();
        pagamentoController = mock();

        paymentListener = new MercadoPagoPaymentListener(mercadoPagoApi, pagamentoController);
    }

    @Test
    void notificarUpdate_noChange() {
        when(mercadoPagoApi.getOrder("http://api.mercadopago.bla/999888777", "999888777")).thenReturn(Map.of(
                "id", "999888777",
                "external_reference", "42",
                "order_status", "waiting"
        ));

        paymentListener.notificarUpdate("999888777", Collections.emptyMap(), Map.of(
                "resource", "http://api.mercadopago.bla/999888777",
                "topic", "payment"
        ));

        verify(pagamentoController, never()).finalizarPagamento(anyInt(), anyString());
    }

    @Test
    void notificarUpdate_changedToPaid() {
        when(mercadoPagoApi.getOrder("http://api.mercadopago.bla/999888777", "999888777")).thenReturn(Map.of(
                "id", "999888777",
                "external_reference", "42",
                "order_status", "paid"
        ));

        paymentListener.notificarUpdate("999888777", Collections.emptyMap(), Map.of(
                "resource", "http://api.mercadopago.bla/999888777",
                "topic", "paid"
        ));

        verify(pagamentoController).finalizarPagamento(42, "999888777");
    }
}