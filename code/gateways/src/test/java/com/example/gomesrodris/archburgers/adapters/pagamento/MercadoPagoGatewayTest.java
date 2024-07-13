package com.example.gomesrodris.archburgers.adapters.pagamento;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.external.FormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.TipoItemCardapio;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import com.example.gomesrodris.archburgers.testUtils.TestLocale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MercadoPagoGatewayTest {
    private MercadoPagoApi mercadoPagoApi;

    private MercadoPagoGateway gateway;

    @BeforeAll
    static void beforeAll() {
        TestLocale.setDefault();
    }

    @BeforeEach
    void setUp() {
        mercadoPagoApi = mock();

        gateway = new MercadoPagoGateway(mercadoPagoApi);
    }

    @Test
    void iniciarRegistroPagamento() {
        var pedido = Pedido.pedidoRecuperado(42, null, "Wanderley", List.of(
                        new ItemPedido(1,
                                new ItemCardapio(3, TipoItemCardapio.ACOMPANHAMENTO, "Batatas Fritas P", "Batatas fritas porção pequena", new ValorMonetario("8.00"))
                        ),
                        new ItemPedido(2,
                                new ItemCardapio(6, TipoItemCardapio.BEBIDA, "Chá gelado", "Chá gelado com limão, feito na casa", new ValorMonetario("6.00"))
                        )
                ), "Batatas com muito sal",
                StatusPedido.PAGAMENTO, new IdFormaPagamento("MercadoPago"),
                LocalDateTime.of(2024, 7, 13, 15, 10, 0));

        when(mercadoPagoApi.getNotificationUrl()).thenReturn("https://architect-burgers.com/mercado-pago-webhook");

        Map<String, Object> expectedPayload = Map.ofEntries(
                entry("title", "Pedido 42"),
                entry("description", "Pedido 42. Wanderley"),
                entry("expiration_date", "2024-07-13T15:20:00.000-03:00"),
                entry("external_reference", "42"),
                entry("notification_url", "https://architect-burgers.com/mercado-pago-webhook"),
                entry("total_amount", 14.00),
                entry("items", List.of(
                                Map.ofEntries(
                                        entry("sku_number", "3"),
                                        entry("category", "food"),
                                        entry("title", "Batatas Fritas P"),
                                        entry("description", "Batatas fritas porção pequena"),
                                        entry("unit_price", 8.0),
                                        entry("quantity", 1),
                                        entry("unit_measure", "unit"),
                                        entry("total_amount", 8.0)),
                                Map.ofEntries(
                                        entry("sku_number", "6"),
                                        entry("category", "food"),
                                        entry("title", "Chá gelado"),
                                        entry("description", "Chá gelado com limão, feito na casa"),
                                        entry("unit_price", 6.0),
                                        entry("quantity", 1),
                                        entry("unit_measure", "unit"),
                                        entry("total_amount", 6.0))
                        )
                ));

        when(mercadoPagoApi.postOrder(expectedPayload)).thenReturn(Map.of(
                "in_store_order_id", "787e9685-7de5-43f1-b09a-6d70f6f6c1e4",
                "qr_data", "00020101021243650016COM.MERCADOLIBRE020130636787e9685"
        ));

        var result = gateway.iniciarRegistroPagamento(pedido);

        verify(mercadoPagoApi).postOrder(expectedPayload);

        assertThat(result).isEqualTo(new FormaPagamento.InfoPagamentoExterno(
                "00020101021243650016COM.MERCADOLIBRE020130636787e9685", null
        ));
    }

    @Test
    public void formatExpirationDate() {
        assertThat(gateway.formatFullDate(LocalDateTime.of(2024, 7, 13, 15, 10, 7)))
                .isEqualTo("2024-07-13T15:10:07.000-03:00");
    }
}