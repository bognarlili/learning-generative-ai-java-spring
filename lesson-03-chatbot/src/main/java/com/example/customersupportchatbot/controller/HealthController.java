package com.example.customersupportchatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    // Constructor injection of our service
    @Autowired
    public HealthController() {
    }

    /**
     * Simple health check endpoint to verify the API is running
     *
     * @return A message indicating the API is up and running
     */
    @GetMapping(value = {"", "/"})
    public String health() {
        return "Chatbot API is up and running!";
    }
}