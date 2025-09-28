package com.example.customersupportchatbot.controller;
import com.example.customersupportchatbot.model.ChatRequest;
import com.example.customersupportchatbot.model.ChatResponse;
import com.example.customersupportchatbot.service.GeminiAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {
    private final GeminiAIService geminiAIService;
    // Constructor injection of our service
    @Autowired
    public ChatbotController(GeminiAIService geminiAIService) {
        this.geminiAIService = geminiAIService;
    }
    /**
     * Endpoint that receives customer queries and returns AI-generated responses
     *
     * @param request The chat request containing the user's query
     * @return A response with the AI-generated answer
     */
    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        // Validate the incoming request
        if (request.getQuery() == null || request.getQuery().trim().isEmpty()) {
            return new ChatResponse("Please provide a valid question or message.");
        }
        // Get a response from the Gemini AI service
        String aiResponse = geminiAIService.generateResponse(request.getQuery());
        // Return the response to the client
        return new ChatResponse(aiResponse);
    }
}