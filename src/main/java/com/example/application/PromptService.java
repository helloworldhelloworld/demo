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

        if (!history.isEmpty()) {
            String joined = history.stream()
                    .map(ChatMessage::getContent)
                    .collect(Collectors.joining("\n"));
            var scenario = templateService.getScenario("mcp");
            if (scenario != null && scenario.getSystem() != null) {
                String memory = templateService.renderTemplate(scenario.getSystem(), Map.of("history", joined));
                messages.add(new SystemMessage(memory));
            }
            messages.addAll(history);
        }

        String userContent = userMessage;
        List<Document> docs = documentUseCase.search(userMessage);
        if (!docs.isEmpty()) {
            Map<String, String> vars = Map.of("context", docs.get(0).getText(), "message", userMessage);
            var scenario = templateService.getScenario("rag");
            if (scenario != null && scenario.getUser() != null) {
                userContent = templateService.renderTemplate(scenario.getUser(), vars);
            }
            String context = null;
            if (scenario != null && scenario.getSystem() != null) {
                context = templateService.renderTemplate(scenario.getSystem(), vars);
            }
            if (context == null) {
                context = templateService.render("context", vars);
            }
            if (context != null) {
                messages.add(new SystemMessage(context));
            }
        } else {
            var scenario = templateService.getScenario("mcp");
            if (scenario != null && scenario.getUser() != null) {
                userContent = templateService.renderTemplate(scenario.getUser(), Map.of("message", userMessage));
            }
        }

        messages.add(new UserMessage(userContent));

        return new Prompt(messages);
    }
}
