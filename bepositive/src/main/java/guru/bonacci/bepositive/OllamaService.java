package guru.bonacci.bepositive;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OllamaService {

    private final RestClient restClient;
    private final String model;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OllamaService(@Value("${ollama.base-url}") String baseUrl,
                         @Value("${ollama.model}") String model) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.model = model;
    }

    public AnalysisResult analyze(String content) {
        String systemPrompt = """
                You are a positivity evaluator. Analyze the following message and determine if it is positive.
                Respond ONLY with a JSON object (no markdown, no extra text) in this exact format:
                {"accepted": true/false, "reason": "brief explanation", "suggestedRewrite": "a positive version if rejected, or empty string if accepted"}
                """;

        String prompt = "Evaluate this message for positivity: \"%s\"".formatted(content);

        Map<String, Object> request = Map.of(
                "model", model,
                "prompt", prompt,
                "system", systemPrompt,
                "stream", false
        );

        String responseBody = restClient.post()
                .uri("/api/generate")
                .body(request)
                .retrieve()
                .body(String.class);

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            String llmResponse = root.get("response").asText();

            // Strip markdown code fences if present
            String json = llmResponse.strip();
            if (json.startsWith("```")) {
                json = json.replaceAll("(?s)```(?:json)?\\s*", "").replaceAll("(?s)```\\s*$", "").strip();
            }

            JsonNode result = objectMapper.readTree(json);
            return new AnalysisResult(
                    result.get("accepted").asBoolean(),
                    result.get("reason").asText(),
                    result.has("suggestedRewrite") ? result.get("suggestedRewrite").asText() : ""
            );
        } catch (Exception e) {
            return new AnalysisResult(false, "Failed to analyze message: " + e.getMessage(), "");
        }
    }
}
