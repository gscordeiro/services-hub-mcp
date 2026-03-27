package dev.gscordeiro.serviceshubmcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class RestToolExecutor {

    Logger logger = LoggerFactory.getLogger(RestToolExecutor.class);
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public Object execute(ToolDefinition def, Map<String, Object> args) {

        logger.info("Executing tool: {}", def.name());

        validateRequired(def, args);

        try {
            String url = buildUrl(def.endpoint(), args);

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url));

            switch (def.method().toUpperCase()) {
                case "GET" -> builder.GET();
                case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString("{}"));
                default -> throw new UnsupportedOperationException("Método não suportado: " + def.method());
            }

            HttpResponse<String> response =
                    httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());

            return Map.of(
                    "status", response.statusCode(),
                    "body", response.body()
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao executar tool " + def.name(), e);
        }
    }

    private void validateRequired(ToolDefinition def, Map<String, Object> args) {

        logger.debug("Validating required parameters for tool: {}", def.name());

        if (def.jsonSchema().contains("\"required\"")) {
            if (def.jsonSchema().contains("\"id\"") && !args.containsKey("id")) {
                throw new IllegalArgumentException("Missing required parameter: id");
            }
        }
    }

    private String buildUrl(String endpoint, Map<String, Object> args) {

        logger.debug("Building URL for tool: {}", endpoint);

        String url = endpoint;

        for (var entry : args.entrySet()) {
            url = url.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }

        return url;
    }
}
