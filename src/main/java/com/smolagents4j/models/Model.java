package com.smolagents4j.models;

import org.springframework.ai.converter.BeanOutputConverter;

public interface Model {

    ModelOutput ask(String task) throws IllegalStateException;

    default String buildPrompt(String prompt, BeanOutputConverter<ModelOutput> outputConverter) {
        return """
                You are a ReAct-style agent. Read the conversation transcript and return exactly one next step as JSON.

                Rules:
                - Return valid JSON only. Do not wrap it in markdown.
                - If a tool should be used next, set shouldPerformToolCall=true, fill thought, actionName, actionInput, set finalAnswer to an empty string, and set isCompleted=false.
                - If the task is complete, set shouldPerformToolCall=false, set finalAnswer, and set isCompleted=true.
                - Keep thought concise.
                - Use empty strings for unused text fields instead of explanations outside the JSON.

                Output format:
                %s

                Conversation transcript:
                %s
                """.formatted(outputConverter.getFormat(), prompt);
    }

    default ModelOutput normalize(ModelOutput output) {
        boolean shouldPerformToolCall = output.shouldPerformToolCall();
        boolean isCompleted = output.isCompleted();

        String actionName = shouldPerformToolCall ? output.actionName() : "";
        String actionInput = shouldPerformToolCall ? output.actionInput() : "";
        String finalAnswer = isCompleted ? output.finalAnswer() : output.finalAnswer();

        return new ModelOutput(
                output.thought(),
                actionName,
                actionInput,
                shouldPerformToolCall,
                finalAnswer,
                isCompleted
        );
    }
}
