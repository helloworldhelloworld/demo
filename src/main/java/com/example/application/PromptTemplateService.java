package com.example.application;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class PromptTemplateService {

    private final PromptProperties properties;

    public PromptTemplateService(PromptProperties properties) {
        this.properties = properties;
    }

    public PromptProperties.Scenario getScenario(String name) {
        return properties.getScenarios().get(name);
    }

    public String render(String name, Map<String, String> variables) {
        String template = properties.getTemplates().get(name);
        if (template == null) {
            return null;
        }
        return renderTemplate(template, variables);
    }

    public String renderTemplate(String template, Map<String, String> variables) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }
}
