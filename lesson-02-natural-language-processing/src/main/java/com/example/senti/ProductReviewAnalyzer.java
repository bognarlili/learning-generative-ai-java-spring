package com.example.senti;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;


import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ProductReviewAnalyzer {

    private StanfordCoreNLP pipeline;

    public static void main(String[] args) {
        ProductReviewAnalyzer analyzer = new ProductReviewAnalyzer();
        analyzer.initialize();
        
        try {
            // Load reviews from CSV
            List<String[]> reviews = analyzer.loadReviews("C:\\Users\\bogna\\IdeaProjects\\gxbpy-java_sentiment_analyzer\\src\\main\\resources\\new_products_review.csv");
            Map<String, Integer> sentimentDistribution = new LinkedHashMap<>();
            sentimentDistribution.put("Very Positive", 0);
            sentimentDistribution.put("Positive", 0);
            sentimentDistribution.put("Neutral", 0);
            sentimentDistribution.put("Negative", 0);
            sentimentDistribution.put("Very Negative", 0);
            
            System.out.println("=== Review Sentiment Analysis ===");
            for (String[] review : reviews) {
                if (review[0].equals("review_id")) continue; // Skip header
                
                String sentiment = analyzer.analyzeSentiment(review[1]);
                sentimentDistribution.put(sentiment, sentimentDistribution.get(sentiment) + 1);
                
                System.out.printf("Review %2s: %-60s - %s\n", 
                    review[0], 
                    shortenText(review[1], 55), 
                    sentiment);
            }
            
            // Generate report
            analyzer.generateReport(sentimentDistribution, reviews.size() - 1);
            
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        // Set up pipeline properties
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        props.setProperty("coref.algorithm", "neural");
        this.pipeline = new StanfordCoreNLP(props);
    }

    public List<String[]> loadReviews(String filePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            return reader.readAll();
        }
    }

    public String analyzeSentiment(String text) {
        int mainSentiment = 0;
        int longest = 0;
        
        Annotation annotation = pipeline.process(text);
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            int sentiment = RNNCoreAnnotations.getPredictedClass(
                sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class));
            String partText = sentence.toString();
            
            if (partText.length() > longest) {
                mainSentiment = sentiment;
                longest = partText.length();
            }
        }
        
        // Convert numeric sentiment to text
        switch (mainSentiment) {
            case 0: return "Very Negative";
            case 1: return "Negative";
            case 2: return "Neutral";
            case 3: return "Positive";
            case 4: return "Very Positive";
            default: return "Neutral";
        }
    }

    public void generateReport(Map<String, Integer> sentimentCounts, int totalReviews) {
        System.out.println("\n=== Sentiment Analysis Report ===");
        System.out.printf("Total Reviews Analyzed: %d\n\n", totalReviews);
        
        System.out.println("Sentiment Distribution:");
        for (Map.Entry<String, Integer> entry : sentimentCounts.entrySet()) {
            double percentage = (entry.getValue() * 100.0) / totalReviews;
            System.out.printf("%-12s: %2d reviews (%5.1f%%) %s\n",
                entry.getKey(),
                entry.getValue(),
                percentage,
                generateBar(percentage));
        }
    }
    
    private static String generateBar(double percentage) {
        int bars = (int) (percentage / 5);
        return "[" + new String(new char[bars]).replace("\0", "=") + "]";
    }
    
    private static String shortenText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
}