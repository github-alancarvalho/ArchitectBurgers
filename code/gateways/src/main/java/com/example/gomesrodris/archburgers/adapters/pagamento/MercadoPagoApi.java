package com.example.gomesrodris.archburgers.adapters.pagamento;

import com.example.gomesrodris.archburgers.domain.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@Service
public class MercadoPagoApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(MercadoPagoApi.class);

    private final String apiBaseUrl;
    private final String userId;
    private final String accessToken;
    private final String posId;
    private final String notificationUrl;

    private final HttpClient httpClient;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public MercadoPagoApi(Environment environment) {
        String apiBaseUrlEnv = environment.getProperty("archburgers.integration.mercadopago.apiBaseUrl");
        String userIdEnv = environment.getProperty("archburgers.integration.mercadopago.userId");
        String accessTokenEnv = environment.getProperty("archburgers.integration.mercadopago.accessToken");
        String posIdEnv = environment.getProperty("archburgers.integration.mercadopago.posId");
        String notificationUrlEnv = environment.getProperty("archburgers.integration.mercadopago.notificationUrl");

        this.apiBaseUrl = Objects.requireNonNull(apiBaseUrlEnv, "archburgers.integration.mercadopago.apiBaseUrl not set");
        this.userId = Objects.requireNonNull(userIdEnv, "archburgers.integration.mercadopago.userId not set");
        this.accessToken = Objects.requireNonNull(accessTokenEnv, "archburgers.integration.mercadopago.accessToken not set");
        this.posId = Objects.requireNonNull(posIdEnv, "archburgers.integration.mercadopago.posId not set");
        this.notificationUrl = Objects.requireNonNull(notificationUrlEnv, "archburgers.integration.mercadopago.notificationUrl not set");

        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public Map<String, Object> postOrder(Map<String, Object> data) {
        var uriStr = apiBaseUrl + "/instore/orders/qr/seller/collectors/" + userId + "/pos/" + posId + "/qrs";
        URI uri;
        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI error: " + uriStr, e);
        }

        String payload;
        try {
            payload = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error creating payload: " + e, e);
        }

        var webRequest = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        try {
            return executeRequest(webRequest);
        } catch (Exception e) {
            LOGGER.error("Error in MercadoPago create Order request: [{}] -- {}", uri, payload);
            if (e instanceof RuntimeException) {
                throw e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public Map<String, Object> getOrder(String resourceUrl, String externalOrderId) {
        String uriStr;
        if (StringUtils.isNotEmpty(resourceUrl)) {
            // If the update notification provided a URL, use it
            uriStr = resourceUrl;
        } else {
            // Use a default url
            uriStr = apiBaseUrl + "/merchant_orders/" + externalOrderId;
        }

        URI uri;
        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI error: " + uriStr, e);
        }

        var webRequest = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        return executeRequest(webRequest);
    }

    public String getNotificationUrl() {
        return notificationUrl;
    }

    private Map<String, Object> executeRequest(HttpRequest webRequest) {
        HttpResponse<String> response;
        try {
            response = httpClient.send(webRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("Error sending order request: " + e, e);
        }

        String body = response.body();

        if (response.statusCode() != 200 && response.statusCode() != 201 && response.statusCode() != 204) {
            throw new RuntimeException("Error in order request: " + response + " -- " + body);
        }

        Map<String, Object> responseData;
        try {
            responseData = mapper.readValue(body, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing response data: " + e + " -- " + body, e);
        }
        return responseData;
    }
}
