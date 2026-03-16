package com.smolagents4j.models;

public record ModelOutput(
        String thought,
        String actionName,
        String actionInput,
        Boolean shouldPerformToolCall,
        String finalAnswer,
        Boolean isCompleted) {
}
