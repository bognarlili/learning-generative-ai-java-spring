package com.example.customersupportchatbot.model;
import java.util.List;
public class GeminiResponse {
    private List<Candidate> candidates;
    // Default constructor
    public GeminiResponse() {
    }
    // Constructor with parameters
    public GeminiResponse(List<Candidate> candidates) {
        this.candidates = candidates;
    }
    // Getter and setter
    public List<Candidate> getCandidates() {
        return candidates;
    }
    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }
    public static class Candidate {
        private Content content;
        // Default constructor
        public Candidate() {
        }
        // Constructor with parameters
        public Candidate(Content content) {
            this.content = content;
        }
        // Getter and setter
        public Content getContent() {
            return content;
        }
        public void setContent(Content content) {
            this.content = content;
        }
    }
    public static class Content {
        private List<Part> parts;
        // Default constructor
        public Content() {
        }
        // Constructor with parameters
        public Content(List<Part> parts) {
            this.parts = parts;
        }
        // Getter and setter
        public List<Part> getParts() {
            return parts;
        }
        public void setParts(List<Part> parts) {
            this.parts = parts;
        }
    }
    public static class Part {
        private String text;
        // Default constructor
        public Part() {
        }
        // Constructor with parameters
        public Part(String text) {
            this.text = text;
        }
        // Getter and setter
        public String getText() {
            return text;
        }
        public void setText(String text) {
            this.text = text;
        }
    }
}