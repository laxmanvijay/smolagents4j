package com.smolagents4j.models;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.huggingface.HuggingfaceChatModel;
import org.springframework.beans.factory.annotation.Autowired;

public class HuggingFaceModel implements Model {

    private final HuggingfaceChatModel chatModel;
    private final BeanOutputConverter<ModelOutput> outputConverter;

    @Autowired
    public HuggingFaceModel(HuggingfaceChatModel chatModel) {
        this.chatModel = chatModel;
        this.outputConverter = new BeanOutputConverter<>(ModelOutput.class);
    }

    @Override
    public ModelOutput ask(String prompt) throws IllegalStateException {
        String llmPrompt = buildPrompt(prompt, outputConverter);
        ChatResponse response = chatModel.call(new Prompt(llmPrompt));
        String responseText = response.getResult().getOutput().getText();

        if (responseText == null || responseText.isBlank()) {
            throw new IllegalStateException("Received empty response from Hugging Face model");
        }

        try {
            return normalize(outputConverter.convert(responseText));
        } catch (RuntimeException e) {
            throw new IllegalStateException("Failed to convert Hugging Face response into ModelOutput: " + responseText, e);
        }
    }
}
