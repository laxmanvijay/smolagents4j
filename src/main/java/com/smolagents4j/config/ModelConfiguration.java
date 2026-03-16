package com.smolagents4j.config;

import org.springframework.ai.huggingface.HuggingfaceChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.smolagents4j.models.HuggingFaceModel;

@Configuration
public class ModelConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HuggingFaceModel huggingFaceModel(HuggingfaceChatModel chatModel) {
        return new HuggingFaceModel(chatModel);
    }
}
