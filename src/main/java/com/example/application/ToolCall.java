package com.example.application;

import java.util.Map;

import org.springframework.util.StringUtils;

public record ToolCall(String name, Map<String, Object> arguments) {

    public ToolCall(String name, Map<String, Object> arguments) {
        String normalizedName = name == null ? null : name.trim();
        if (!StringUtils.hasText(normalizedName)) {
            throw new IllegalArgumentException("Tool name must not be blank");
        }
        this.name = normalizedName;
        this.arguments = arguments == null ? Map.of() : Map.copyOf(arguments);
    }
}
