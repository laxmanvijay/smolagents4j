package com.smolagents4j.tools;

import com.smolagents4j.exceptions.ToolException;

public interface BaseTool {

    String run(String task) throws ToolException;
}
