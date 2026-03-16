package com.smolagents4j.agents;

import com.smolagents4j.constants.AgentConstants;
import com.smolagents4j.exceptions.MemoryReadException;
import com.smolagents4j.exceptions.MemoryWriteException;
import com.smolagents4j.exceptions.ToolException;
import com.smolagents4j.memory.AgentMemory;
import com.smolagents4j.models.Model;
import com.smolagents4j.models.ModelOutput;
import com.smolagents4j.monitoring.AgentLogger;
import com.smolagents4j.tools.BaseTool;

import java.util.ArrayList;
import java.util.HashMap;

public class Agent {

    String task;
    Model llm;
    HashMap<String, BaseTool> tools = new HashMap<>();
    AgentLogger agentLogger;
    AgentMemory memory;


    Agent(String task, AgentMemory agentMemory, AgentLogger agentLogger) {
        this.task = task;
        this.memory = agentMemory;
        this.agentLogger = agentLogger;
    }

    public void run() {

        ArrayList<String> message = new ArrayList<>();
        message.add(this.task);
        this.agentLogger.log("Initial task: " + this.task);
        ArrayList<ModelOutput> mem = new ArrayList<>();

        try {
            mem = this.memory.retrieveMemory();

            message.add("Memory found for task; Resuming by appending previous messages");
            for (var m : mem) {
                appendIfPresent(message, formatModelOutput(m));
            }
        } catch (MemoryReadException e) {
            this.agentLogger.log("Memory read exception occurred: " + e.getMessage());
        }

        if (mem.isEmpty()) {
            this.agentLogger.log("Memory empty; Starting new task");
            this.memory.addFirstMessage(this.task);
        }

        boolean isCompleted = false;
        int iterationCount = 0;

        while (!isCompleted && iterationCount < AgentConstants.max_agent_iterations) {
            iterationCount++;
            var output = llm.ask(message.stream().reduce("", (x, y) -> x + "; " + y));

            try {
                this.memory.appendToMemory(output);
                this.agentLogger.log("Memory written");
                message.add("Memory written");
            }
            catch (MemoryWriteException e) {
                this.agentLogger.log("Memory write exception occurred: " + e.getMessage());
                message.add("Memory write exception occurred: " + e.getMessage());
            }

            String formattedOutput = formatModelOutput(output);
            appendIfPresent(message, formattedOutput);
            if (!formattedOutput.isBlank()) {
                this.agentLogger.log("Agent output: " + formattedOutput);
            }

            if (output.shouldPerformToolCall()) {
                try {
                    String actionName = output.actionName();
                    String actionInput = output.actionInput();
                    BaseTool tool = tools.get(actionName);

                    this.agentLogger.log("Invoking tool: " + actionName + "; task: " + actionInput);
                    message.add("Observation: " + tool.run(actionInput));
                }
                catch (ToolException e) {
                    message.add("Tool exception thrown: " + e.getMessage());
                    this.agentLogger.log("Tool invocation caused exception: " + e.getMessage());
                }
            }

            if (output.isCompleted()) {
                isCompleted = true;
            }
        }

        if (!isCompleted) {
            this.agentLogger.log("Agent stopped after reaching max iterations: " + AgentConstants.max_agent_iterations);
        }
    }

    private void appendIfPresent(ArrayList<String> message, String content) {
        if (content != null && !content.isBlank()) {
            message.add(content);
        }
    }

    private String formatModelOutput(ModelOutput output) {
        ArrayList<String> parts = new ArrayList<>();

        if (output.thought() != null && !output.thought().isBlank()) {
            parts.add("Thought: " + output.thought());
        }

        if (output.shouldPerformToolCall()) {
            if (output.actionName() != null && !output.actionName().isBlank()) {
                parts.add("Action: " + output.actionName());
            }

            if (output.actionInput() != null && !output.actionInput().isBlank()) {
                parts.add("Action Input: " + output.actionInput());
            }
        }

        if (output.finalAnswer() != null && !output.finalAnswer().isBlank()) {
            parts.add("Final Answer: " + output.finalAnswer());
        }

        return String.join("; ", parts);
    }
}
