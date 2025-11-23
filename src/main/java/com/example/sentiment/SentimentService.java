package com.example.sentiment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

@Service
public class SentimentService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Double> positiveWords = Collections.emptyMap();
    private Map<String, Double> negativeWords = Collections.emptyMap();

    @PostConstruct
    public void loadModel() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/sentiment-model.json")) {
            if (is == null) {
                throw new IllegalStateException("sentiment-model.json not found on classpath");
            }
            Map<String, Map<String, Double>> raw = objectMapper.readValue(
                    is,
                    new TypeReference<Map<String, Map<String, Double>>>() {}
            );
            this.positiveWords = raw.getOrDefault("positive", Collections.emptyMap());
            this.negativeWords = raw.getOrDefault("negative", Collections.emptyMap());
        }
    }

    public SentimentResponse analyze(String text) {
        if (text == null || text.isBlank()) {
            return new SentimentResponse("", "neutral", 0.5);
        }

        String normalized = text.toLowerCase();
        double score = 0.0;

        for (var entry : positiveWords.entrySet()) {
            if (normalized.contains(entry.getKey())) {
                score += entry.getValue();
            }
        }

        for (var entry : negativeWords.entrySet()) {
            if (normalized.contains(entry.getKey())) {
                score -= entry.getValue();
            }
        }

        String sentiment;
        double confidence;
        if (score > 0.2) {
            sentiment = "positive";
            confidence = Math.min(0.5 + score, 0.99);
        } else if (score < -0.2) {
            sentiment = "negative";
            confidence = Math.min(0.5 - score, 0.99);
        } else {
            sentiment = "neutral";
            confidence = 0.6;
        }

        return new SentimentResponse(text, sentiment, confidence);
    }
}
