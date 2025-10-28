package com.example.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

class PromptServiceTests {

    @Test
    void buildPromptAddsTemplates() {
        PromptProperties properties = new PromptProperties();
        properties.getTemplates().put("functioncall", "TOOLS");

        PromptProperties.Scenario mcp = new PromptProperties.Scenario();
        mcp.setSystem("History: {history}");
        mcp.setConstraints("Use MCP for {message}");
        mcp.setUser("Call MCP with {message}");
        properties.getScenarios().put("mcp", mcp);
        PromptTemplateService templateService = new PromptTemplateService(properties);
        PromptService service = new PromptService(templateService);
        var prompt = service.buildPrompt(List.of(new UserMessage("hi")), "q");
        List<Message> messages = prompt.getInstructions();
        assertEquals(5, messages.size());
        assertEquals("TOOLS", ((SystemMessage) messages.get(0)).getText());
        assertEquals("History: hi", ((SystemMessage) messages.get(1)).getText());
        assertEquals("Use MCP for q", ((SystemMessage) messages.get(2)).getText());
        assertEquals("hi", messages.get(3).getText());
        assertEquals("Call MCP with q", messages.get(4).getText());
    }
}
