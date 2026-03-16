package com.smolagents4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.WebApplicationType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.smolagents4j.agents.Agent;
import com.smolagents4j.dao.AgentMemoryJpaRepository;
import com.smolagents4j.memory.AgentMemory;
import com.smolagents4j.memory.PostgresAgentMemory;
import com.smolagents4j.models.HuggingFaceModel;
import com.smolagents4j.monitoring.AgentLogger;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.smolagents4j")
public class Main {

    @Autowired
    private AgentMemoryJpaRepository repository;

    @Autowired
    private HuggingFaceModel huggingFaceModel;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            AgentLogger logger = new AgentLogger();
            AgentMemory memory = new PostgresAgentMemory(repository, "session-123");

            Agent agent = new Agent("Write a haiku about the sea", memory, logger, huggingFaceModel);
            agent.run();

            for (String log : logger.getAllMessages()) {
                System.out.println(log);
            }
        };
    }
}
