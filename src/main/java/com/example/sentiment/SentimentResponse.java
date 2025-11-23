package com.example.sentiment;

public record SentimentResponse(
        String text,
        String sentiment,
        double confidence
) {
}
