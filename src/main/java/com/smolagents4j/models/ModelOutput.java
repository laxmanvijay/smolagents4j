package com.smolagents4j.models;

public record ModelOutput(
        String message,
        String toolName,
        Boolean shouldPerformToolCall,
        String toolTask,
        Boolean isCompleted) {
}
