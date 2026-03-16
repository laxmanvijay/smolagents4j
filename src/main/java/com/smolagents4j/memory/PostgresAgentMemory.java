package com.smolagents4j.memory;

import com.smolagents4j.dao.AgentMemoryJpaRepository;
import com.smolagents4j.dao.AgentMemoryRecord;
import com.smolagents4j.exceptions.MemoryReadException;
import com.smolagents4j.exceptions.MemoryWriteException;
import com.smolagents4j.models.ModelOutput;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class PostgresAgentMemory implements AgentMemory {

    private final AgentMemoryJpaRepository repository;
    private final String sessionId;


    public PostgresAgentMemory(AgentMemoryJpaRepository repository, String sessionId) {
        this.repository = repository;
        this.sessionId = sessionId;
    }

    @Override
    public ArrayList<ModelOutput> retrieveMemory() throws MemoryReadException {
        try {
            return repository.findBySessionIdOrderByIdAsc(sessionId)
                    .stream()
                    .map(this::toModelOutput)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
            throw new MemoryReadException(
                    "Failed to retrieve agent memory [session=" + sessionId + "]: " + e.getMessage());
        }
    }

    @Override
    public void addFirstMessage(String message) {
        AgentMemoryRecord record = new AgentMemoryRecord(sessionId, message, null, null, false, null, null, false);
        try {
            repository.save(record);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to persist first message [session=" + sessionId + "]", e);
        }
    }

    @Override
    public void appendToMemory(ModelOutput modelOutput) throws MemoryWriteException {
        AgentMemoryRecord record = new AgentMemoryRecord(
                sessionId,
                null,
                modelOutput.thought(),
                modelOutput.actionName(),
                modelOutput.shouldPerformToolCall(),
                modelOutput.actionInput(),
                modelOutput.finalAnswer(),
                modelOutput.isCompleted());
        try {
            repository.save(record);
        } catch (Exception e) {
            throw new MemoryWriteException(
                    "Failed to write agent memory [session=" + sessionId + "]: " + e.getMessage());
        }
    }

    private ModelOutput toModelOutput(AgentMemoryRecord record) {
        return new ModelOutput(
                normalizeThought(record),
                record.getActionName(),
                record.getActionInput(),
                record.getShouldPerformToolCall(),
                record.getFinalAnswer(),
                record.getIsCompleted());
    }

    private String normalizeThought(AgentMemoryRecord record) {
        if (record.getThought() != null && !record.getThought().isBlank()) {
            return record.getThought();
        }

        if (isInitialTaskRecord(record)) {
            return null;
        }

        // Older rows may only have a raw message payload.
        return record.getMessage();
    }

    private boolean isInitialTaskRecord(AgentMemoryRecord record) {
        return record.getMessage() != null
                && record.getActionName() == null
                && record.getActionInput() == null
                && record.getFinalAnswer() == null
                && !record.getShouldPerformToolCall()
                && !record.getIsCompleted();
    }
}
