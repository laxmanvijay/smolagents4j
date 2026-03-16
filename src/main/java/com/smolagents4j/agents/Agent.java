package com.smolagents4j.agents;

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
                message.add(m.message());
            }
        } catch (MemoryReadException e) {
            this.agentLogger.log("Memory read exception occurred: " + e.getMessage());
        }

        if (mem.isEmpty()) {
            this.agentLogger.log("Memory empty; Starting new task");
            this.memory.addFirstMessage(this.task);
        }

        boolean isCompleted = false;

        while (!isCompleted) {
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

            message.add(output.message());
            this.agentLogger.log("Agent output: " + output.message());

            if (output.shouldPerformToolCall()) {
                try {
                    this.agentLogger.log("Invoking tool: " + output.toolName() + "; task: " + output.toolTask());
                    message.add(tools.get(output.toolName()).run(output.toolTask()));
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
    }
}
