package com.smolagents4j.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AgentMemoryJpaRepository extends JpaRepository<AgentMemoryRecord, Long> {

    List<AgentMemoryRecord> findBySessionIdOrderByIdAsc(String sessionId);
}
