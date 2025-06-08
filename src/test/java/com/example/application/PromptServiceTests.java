package com.example.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.ChatMessage;
import org.springframework.ai.chat.prompt.SystemMessage;
import org.springframework.ai.chat.prompt.UserMessage;
import org.springframework.ai.document.Document;

class PromptServiceTests {

    @Test
    void buildPromptAddsTemplates() {
        DocumentUseCase documentUseCase = mock(DocumentUseCase.class);
        when(documentUseCase.search("q")).thenReturn(List.of(new Document("id", "doc")));

        PromptProperties properties = new PromptProperties();
        properties.getTemplates().put("functioncall", "TOOLS");
        properties.getTemplates().put("mcp", "History: {history}");
        properties.getTemplates().put("rag", "RAG: {context}");
        PromptTemplateService templateService = new PromptTemplateService(properties);
        PromptService service = new PromptService(documentUseCase, templateService);
        var prompt = service.buildPrompt(List.of(new UserMessage("hi")), "q");
        List<ChatMessage> messages = prompt.getMessages();
        assertEquals(5, messages.size());
        assertEquals("TOOLS", ((SystemMessage) messages.get(0)).getContent());
        assertEquals("History: hi", ((SystemMessage) messages.get(1)).getContent());
        assertEquals("hi", messages.get(2).getContent());
        assertEquals("q", messages.get(3).getContent());
        assertEquals("RAG: doc", ((SystemMessage) messages.get(4)).getContent());
    }
}
