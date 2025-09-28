package com.example.customersupportchatbot.model;

import jakarta.servlet.http.Part;

import javax.swing.text.AbstractDocument;
import java.util.List;

public class GeminiRequest {
    private List<AbstractDocument.Content> contents;

    public GeminiRequest(){

    }

    public GeminiRequest(List<AbstractDocument.Content> contents){
        this.contents=contents;
    }

    public List<AbstractDocument.Content> getContents() {
        return contents;
    }

    public void setContents(List<AbstractDocument.Content> contents) {
        this.contents = contents;
    }

    public static class Content{
        private List<Part> parts;

        public Content(List<Part> parts){
            this.parts = parts;
        }

        public List<Part> getParts() {
            return parts;
        }

        public void setParts(List<Part> parts) {
            this.parts = parts;
        }

        public static class Part{
            private String text;

            public Part(){}

            public Part(String text){
                this.text=text;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }
}
