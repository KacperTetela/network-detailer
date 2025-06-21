package networkdetailer.com.model.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpClient {
    private static HttpClient instance;
    private final java.net.http.HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private static final String API_URL = "https://network-detailer-database-production.up.railway.app/api/scans";
    private static final String ACCESS_KEY = "76833fa3-245a-488b-9200-fcf725fbcf26";

    public static synchronized HttpClient getInstance() {
        if (instance == null) {
            instance = new HttpClient();
        }
        return instance;
    }

    private HttpClient() {
        this.httpClient = java.net.http.HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .version(java.net.http.HttpClient.Version.HTTP_2)
                .build();

        this.objectMapper = new ObjectMapper();
    }

    public boolean sendData(DatabaseDTO data) {
        try {
            String jsonPayload = convertToJson(data);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("X-ACCESS-KEY", ACCESS_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return handleResponse(response);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private String convertToJson(DatabaseDTO data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }

    private boolean handleResponse(HttpResponse<String> response) {
        int statusCode = response.statusCode();
        String body = response.body();

        if (statusCode < 200 || statusCode >= 300) {
            throw new RuntimeException("Error API: " + statusCode + " - " + body);
        }
        return true;
    }
}
