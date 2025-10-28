package com.example.application;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ToolCallParser {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;

    public ToolCallParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Optional<ToolCall> parse(String response) {
        if (!StringUtils.hasText(response)) {
            return Optional.empty();
        }
        try {
            JsonNode node = objectMapper.readTree(response);
            if (!node.isObject()) {
                return Optional.empty();
            }
            String type = text(node, "type");
            if (!"call_tool".equals(type)) {
                return Optional.empty();
            }
            String name = text(node, "name");
            if (!StringUtils.hasText(name)) {
                return Optional.empty();
            }
            JsonNode argumentsNode = node.get("arguments");
            Map<String, Object> arguments = argumentsNode == null || argumentsNode.isNull()
                    ? Map.of()
                    : objectMapper.convertValue(argumentsNode, MAP_TYPE);
            return Optional.of(new ToolCall(name, arguments));
        }
        catch (IOException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    private static String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value == null || value.isNull()) {
            return null;
        }
        String text = value.asText();
        return StringUtils.hasText(text) ? text : null;
    }
}
