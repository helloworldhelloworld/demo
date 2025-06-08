package com.example.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List<ChatMessage> messages = new ArrayList<>();

        String tools = templateService.render("functioncall", Map.of());
        if (tools != null) {
            messages.add(new SystemMessage(tools));
        }

        String react = templateService.render("react", Map.of());
        if (react != null) {
            messages.add(new SystemMessage(react));
        }

        if (!history.isEmpty()) {
            String joined = history.stream()
                    .map(ChatMessage::getContent)
                    .collect(Collectors.joining("\n"));
            String memory = templateService.render("mcp", Map.of("history", joined));
            if (memory != null) {
                messages.add(new SystemMessage(memory));
            }
            messages.addAll(history);
        }

        messages.add(new UserMessage(userMessage));

        List<Document> docs = documentUseCase.search(userMessage);
        if (!docs.isEmpty()) {
            Map<String, String> vars = Map.of("context", docs.get(0).getText());
            String context = templateService.render("rag", vars);
            if (context == null) {
                context = templateService.render("context", vars);
            }
            if (context == null) {
                context = "Context: " + docs.get(0).getText();
            }
            messages.add(new SystemMessage(context));
        }

        return new Prompt(messages);
    }
}
