package com.example.customersupportchatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiAIService {

    private final WebClient webClient;
    private final String apiKey;
    private final String model;

    public GeminiAIService(
            @Value("${gemini.api.key}") String apiKey,
            @Value("${gemini.model:gemini-2.5-flash}") String model
    ) {
        this.apiKey = apiKey;
        this.model  = model;
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Generates a response using the Gemini API.
     *
     * @param query The user's query
     * @return The response from the model
     */
    public String generateResponse(String query) {
        try {
            if (apiKey == null || apiKey.isBlank()) {
                return "Missing Gemini API key. Please set 'gemini.api.key' in application.properties.";
            }
            if (query == null || query.isBlank()) {
                return "Please provide a valid question or message.";
            }

            // Build prompt WITH the user's question
            String fullPrompt =
                    "You are a helpful customer support chatbot. " +
                            "Provide a concise and friendly response to this customer query: " + query;

            // Request body for Gemini generateContent
            Map<String, Object> partObject = new HashMap<>();
            partObject.put("text", fullPrompt);

            Map<String, Object> contentObject = new HashMap<>();
            contentObject.put("role", "user");
            contentObject.put("parts", List.of(partObject));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(contentObject));

            // Debug (kulcs csak részben logolva)
            String keyPreview = apiKey.length() > 6 ? apiKey.substring(0, 6) + "..." : "SHORT/INVALID";
            System.out.println("Sending request to Gemini API (v1). Key: " + keyPreview + ", model: " + model);
            System.out.println("Request body: " + requestBody);

            // v1 endpoint + model property + API key in header
            Map<String, Object> response = webClient.post()
                    .uri("/v1/models/{model}:generateContent", model)
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            System.out.println("Response received: " + response);

            // Extract text from response
            if (response != null && response.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                    if (content != null) {
                        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                        if (parts != null && !parts.isEmpty()) {
                            Object text = parts.get(0).get("text");
                            if (text instanceof String) {
                                return (String) text;
                            }
                        }
                    }
                }
            }

            return "I'm sorry, I couldn't process your request.";

        } catch (WebClientResponseException e) {
            // Specific HTTP errors
            System.err.println("Error calling Gemini API: " + e.getStatusCode() + " " + e.getStatusText());
            System.err.println("Response body: " + e.getResponseBodyAsString());

            int code = e.getStatusCode().value();
            if (code == 400) {
                return "I'm sorry, there was an error with the request format (400 Bad Request). " +
                        "Please ensure your API key and request body are correct, then try again.";
            } else if (code == 401 || code == 403) {
                return "I'm sorry, there was an authentication/authorization error. Please check your API key and project setup.";
            } else if (code == 404) {
                return "I'm sorry, the model endpoint was not found (404). " +
                        "Set 'gemini.model' to a model your key can access (e.g., 'gemini-2.5-flash').";
            } else if (code == 429) {
                return "I'm sorry, we've hit the rate limit for the Gemini API (429). Please try again in a minute.";
            } else if (code >= 500) {
                return "I'm sorry, the Gemini service is currently unavailable (" + code + "). Please try again later.";
            } else {
                return "I'm sorry, there was an error calling the Gemini API: " +
                        e.getStatusCode() + " " + e.getStatusText();
            }

        } catch (Exception e) {
            System.err.println("Unexpected error calling Gemini API: " + e.getMessage());
            e.printStackTrace();
            return "I'm sorry, there was an unexpected error processing your request: " + e.getMessage();
        }
    }
}
