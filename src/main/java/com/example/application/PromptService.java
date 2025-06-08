package com.example.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.prompt.ChatMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemMessage;
import org.springframework.ai.chat.prompt.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

@Service
public class PromptService {

    private final DocumentUseCase documentUseCase;
    private final PromptTemplateService templateService;

    public PromptService(DocumentUseCase documentUseCase, PromptTemplateService templateService) {
        this.documentUseCase = documentUseCase;
        this.templateService = templateService;
    }

    public Prompt buildPrompt(List<ChatMessage> history, String userMessage) {
        List<ChatMessage> messages = new ArrayList<>(history);
        messages.add(new UserMessage(userMessage));

        List<Document> docs = documentUseCase.search(userMessage);
        if (!docs.isEmpty()) {
            String context = templateService.render("context", Map.of("context", docs.get(0).getText()));
            if (context == null) {
                context = "Context: " + docs.get(0).getText();
            }
            messages.add(new SystemMessage(context));
        }

        return new Prompt(messages);
    }
}
