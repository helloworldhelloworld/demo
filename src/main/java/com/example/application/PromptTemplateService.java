package com.example.application;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class PromptTemplateService {

    private final PromptProperties properties;

    public PromptTemplateService(PromptProperties properties) {
        this.properties = properties;
    }

    public String render(String name, Map<String, String> variables) {
        String template = properties.getTemplates().get(name);
        if (template == null) {
            return null;
        }
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }
}
