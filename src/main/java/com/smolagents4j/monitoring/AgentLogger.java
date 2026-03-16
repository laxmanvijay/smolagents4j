package com.smolagents4j.monitoring;

import java.util.ArrayList;

public class AgentLogger {
    ArrayList<String> messages = new ArrayList<>();

    public void log(String message) {
        messages.add(message);
    }

    public ArrayList<String> getAllMessages() {
        return messages;
    }
}
