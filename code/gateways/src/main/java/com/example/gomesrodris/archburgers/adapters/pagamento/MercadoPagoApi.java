package com.example.gomesrodris.archburgers.adapters.pagamento;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public class MercadoPagoApi {
    private final String apiBaseUrl;
    private final String userId;
    private final String accessToken;
    private final String posId;
    private final String notificationUrl;

    private final HttpClient httpClient;

    private final ObjectMapper mapper = new ObjectMapper();

    public MercadoPagoApi(String apiBaseUrl, String userId, String accessToken, String posId, String notificationUrl) {
        this.apiBaseUrl = apiBaseUrl;
        this.userId = userId;
        this.accessToken = accessToken;
        this.posId = posId;
        this.notificationUrl = notificationUrl;

        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public Map<?, ?> postOrder(Map<String, String> data) {
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

        HttpResponse<String> response;
        try {
            response = httpClient.send(webRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("Error sending order request: " + e, e);
        }

        String body = response.body();

        if (response.statusCode() != 200 && response.statusCode() != 204) {
            throw new RuntimeException("Error in order request: " + response + " -- " + body);
        }

        Map<?, ?> responseData;
        try {
            responseData = mapper.readValue(body, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing response data: " + e + " -- " + body, e);
        }

        return responseData;
    }

    public String getNotificationUrl() {
        return notificationUrl;
    }
}
