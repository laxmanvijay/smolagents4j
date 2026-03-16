package com.smolagents4j.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link AgentMemoryRecord}.
 *
 * <p>Spring generates the implementation at runtime; callers only depend on this
 * interface.  The derived query method name is enough for Spring Data to produce
 * the correct {@code SELECT … WHERE session_id = ? ORDER BY id ASC} query
 * without any JPQL or native SQL.
 */
public interface AgentMemoryJpaRepository extends JpaRepository<AgentMemoryRecord, Long> {

    /**
     * Returns all memory steps that belong to the given session, ordered by
     * insertion sequence (primary key ascending = oldest first).
     *
     * @param sessionId the agent session identifier
     * @return ordered list of memory records; never {@code null}
     */
    List<AgentMemoryRecord> findBySessionIdOrderByIdAsc(String sessionId);
}
