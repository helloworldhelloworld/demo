package com.example.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PromptService {

    private final PromptTemplateService templateService;

    public PromptService(PromptTemplateService templateService) {
        this.templateService = templateService;
    }

    public Prompt buildPrompt(List<Message> history, String userMessage) {
        Map<String, String> variables = buildVariables(userMessage, joinHistory(history));

        List<Message> messages = new ArrayList<>();
        addToolsInstruction(messages);

        PromptProperties.Scenario mcpScenario = templateService.getScenario("mcp");
        if (!history.isEmpty()) {
            String memory = renderScenario(mcpScenario, PromptProperties.Scenario::getSystem, variables);
            if (StringUtils.hasText(memory)) {
                messages.add(new SystemMessage(memory));
            }
        }

        String mcpConstraints = renderScenario(mcpScenario, PromptProperties.Scenario::getConstraints, variables);
        if (StringUtils.hasText(mcpConstraints)) {
            messages.add(new SystemMessage(mcpConstraints));
        }

        if (!history.isEmpty()) {
            messages.addAll(history);
        }

        String mcpUser = renderScenario(mcpScenario, PromptProperties.Scenario::getUser, variables);
        String userContent = StringUtils.hasText(mcpUser) ? mcpUser : userMessage;
        messages.add(new UserMessage(userContent));

        return new Prompt(messages);
    }

    private void addToolsInstruction(List<Message> messages) {
        String tools = templateService.render("functioncall", Map.of());
        if (StringUtils.hasText(tools)) {
            messages.add(new SystemMessage(tools));
        }
    }

    private Map<String, String> buildVariables(String userMessage, String historyText) {
        Map<String, String> variables = new HashMap<>();
        variables.put("message", userMessage);
        putIfHasText(variables, "history", historyText);
        return variables;
    }

    private static void putIfHasText(Map<String, String> variables, String key, String value) {
        if (StringUtils.hasText(value)) {
            variables.put(key, value);
        }
    }

    private String joinHistory(List<Message> history) {
        if (history.isEmpty()) {
            return null;
        }
        return history.stream()
                .map(Message::getText)
                .collect(Collectors.joining("\n"));
    }

    private String renderScenario(PromptProperties.Scenario scenario,
            Function<PromptProperties.Scenario, String> extractor, Map<String, String> variables) {
        if (scenario == null) {
            return null;
        }
        String template = extractor.apply(scenario);
        if (!StringUtils.hasText(template)) {
            return null;
        }
        return templateService.renderTemplate(template, variables);
    }
}
