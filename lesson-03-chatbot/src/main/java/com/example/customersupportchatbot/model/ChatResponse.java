package com.example.customersupportchatbot.model;
public class ChatResponse {
    private String response;

    // Default constructor
    public ChatResponse() {
    }

    // Constructor with parameters
    public ChatResponse(String response) {
        this.response = response;
    }

    // Getter and setter
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}