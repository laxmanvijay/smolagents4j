package com.smolagents4j.memory;

import com.smolagents4j.exceptions.MemoryReadException;
import com.smolagents4j.exceptions.MemoryWriteException;
import com.smolagents4j.models.ModelOutput;

import java.util.ArrayList;

public interface AgentMemory {

    ArrayList<ModelOutput> retrieveMemory() throws MemoryReadException;
    void appendToMemory(ModelOutput modelOutput) throws MemoryWriteException;
    void addFirstMessage(String message);
}
