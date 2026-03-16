package com.smolagents4j.dao;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

/**
 * JPA entity that maps to the {@code agent_memory} table.
 *
 * <p>Each row represents one {@link com.smolagents4j.models.ModelOutput} step
 * produced during an agent run, scoped to a {@code sessionId} so that multiple
 * runs can share the same database without interfering with each other.
 */
@Entity
@Table(
        name = "agent_memory",
        indexes = @Index(name = "idx_agent_memory_session_id", columnList = "session_id")
)
public class AgentMemoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "thought", columnDefinition = "TEXT")
    private String thought;

    @Column(name = "action_name")
    private String actionName;

    @Column(name = "should_perform_tool_call")
    private Boolean shouldPerformToolCall;

    @Column(name = "action_input", columnDefinition = "TEXT")
    private String actionInput;

    @Column(name = "final_answer", columnDefinition = "TEXT")
    private String finalAnswer;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    // -----------------------------------------------------------------------
    // Lifecycle
    // -----------------------------------------------------------------------

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    /** Required by JPA. */
    protected AgentMemoryRecord() {
    }

    public AgentMemoryRecord(
            String sessionId,
            String message,
            String thought,
            String actionName,
            Boolean shouldPerformToolCall,
            String actionInput,
            String finalAnswer,
            Boolean isCompleted) {
        this.sessionId = sessionId;
        this.message = message;
        this.thought = thought;
        this.actionName = actionName;
        this.shouldPerformToolCall = shouldPerformToolCall;
        this.actionInput = actionInput;
        this.finalAnswer = finalAnswer;
        this.isCompleted = isCompleted;
    }

    // -----------------------------------------------------------------------
    // Getters
    // -----------------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getMessage() {
        return message;
    }

    public String getThought() {
        return thought;
    }

    public String getActionName() {
        return actionName;
    }

    public Boolean getShouldPerformToolCall() {
        return shouldPerformToolCall;
    }

    public String getActionInput() {
        return actionInput;
    }

    public String getFinalAnswer() {
        return finalAnswer;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
