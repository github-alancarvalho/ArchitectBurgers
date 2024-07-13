package com.example.gomesrodris.archburgers.adapters.pagamento;

import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.external.FormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class MercadoPagoGateway implements FormaPagamento {
    private static final Logger LOGGER = LoggerFactory.getLogger(MercadoPagoGateway.class);

    private final MercadoPagoApi mercadoPagoApi;

    private final DateTimeFormatter fullDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private final Pattern fixTimeZonePattern = Pattern.compile("([+-])(\\d{2})(\\d{2})$");

    public MercadoPagoGateway(MercadoPagoApi mercadoPagoApi) {
        this.mercadoPagoApi = mercadoPagoApi;
    }

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
        Map<String, Object> data = new HashMap<>();

        var expiration = pedido.dataHoraPedido().plusMinutes(10);

        data.put("title", "Pedido " + pedido.id());
        data.put("description", "Pedido " + pedido.id() + ". "
                + (pedido.idClienteIdentificado() != null ? "ClienteId=" + pedido.idClienteIdentificado().id() : pedido.nomeClienteNaoIdentificado()));
        data.put("expiration_date", formatFullDate(expiration));
        data.put("external_reference", String.valueOf(pedido.id()));
        data.put("notification_url", mercadoPagoApi.getNotificationUrl());
        data.put("total_amount", pedido.getValorTotal().asBigDecimal().doubleValue());

        var itens = new ArrayList<Map<String, Object>>();

        for (ItemPedido itemPedido : pedido.itens()) {
            itens.add(convertItemData(itemPedido));
        }
        data.put("items", itens);

        Map<String, Object> apiResult = mercadoPagoApi.postOrder(data);

        return new InfoPagamentoExterno(
                (String) apiResult.get("qr_data"),
                null  // Id pedido nao disponível no retorno inicial, sera enviado ao webhook
        );
    }

    @VisibleForTesting
    String formatFullDate(LocalDateTime dateTime) {
        var formatted = dateTime.atZone(ZoneId.systemDefault()).format(fullDateFormat);
        // Adjust Timezone format at the end.
        // Standard lib produces like ...00.000-0300  . API requires ...00.000-03:00

        var matcher = fixTimeZonePattern.matcher(formatted);
        if (matcher.find()) {
            return matcher.replaceAll(matcher.group(1) + matcher.group(2) + ":" + matcher.group(3));
        } else {
            return formatted;
        }
    }

    private static @NotNull Map<String, Object> convertItemData(ItemPedido itemPedido) {
        double valor = itemPedido.itemCardapio().valor().asBigDecimal().doubleValue();

        Map<String, Object> dataItem = new HashMap<>();

        dataItem.put("sku_number", String.valueOf(itemPedido.itemCardapio().id()));
        dataItem.put("category", "food");
        dataItem.put("title", itemPedido.itemCardapio().nome());
        dataItem.put("description", itemPedido.itemCardapio().descricao());
        dataItem.put("unit_price", valor);
        dataItem.put("quantity", 1);
        dataItem.put("unit_measure", "unit");
        dataItem.put("total_amount", valor);
        return dataItem;
    }
}
