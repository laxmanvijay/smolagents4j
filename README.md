# smolagents4j

A simple ReAct style agents library for Java.

WIP

### Example flow:

```java
@Autowired
    private AgentMemoryJpaRepository repository;

@Autowired
private HuggingFaceModel huggingFaceModel;

AgentLogger logger = new AgentLogger();
AgentMemory memory = new PostgresAgentMemory(repository, "session-123");

Agent agent = new Agent("Write a haiku about the sea", memory, logger, huggingFaceModel);
agent.run();

for (String log : logger.getAllMessages()) {
    System.out.println(log);
}
```
