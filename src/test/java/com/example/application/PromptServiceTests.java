package com.example.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.ChatMessage;
import org.springframework.ai.chat.prompt.SystemMessage;
import org.springframework.ai.document.Document;

class PromptServiceTests {

    @Test
    void buildPromptAddsContext() {
        DocumentUseCase documentUseCase = mock(DocumentUseCase.class);
        when(documentUseCase.search("q")).thenReturn(List.of(new Document("id", "doc")));

        PromptProperties properties = new PromptProperties();
        properties.getTemplates().put("context", "Context: {context}");
        PromptTemplateService templateService = new PromptTemplateService(properties);
        PromptService service = new PromptService(documentUseCase, templateService);
        var prompt = service.buildPrompt(List.of(), "q");
        List<ChatMessage> messages = prompt.getMessages();
        assertEquals(2, messages.size());
        assertEquals("q", messages.get(0).getContent());
        assertEquals("Context: doc", ((SystemMessage) messages.get(1)).getContent());
    }
}
