package com.example.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class ToolCallParserTests {

    private final ToolCallParser parser = new ToolCallParser(new ObjectMapper());

    @Test
    void parsesValidToolCall() {
        String payload = "{\"type\":\"call_tool\",\"name\":\"currentTime\",\"arguments\":{\"timezone\":\"UTC\"}}";
        ToolCall call = parser.parse(payload).orElseThrow();
        assertEquals("currentTime", call.name());
        assertEquals(Map.of("timezone", "UTC"), call.arguments());
    }

    @Test
    void ignoresNonToolResponses() {
        assertTrue(parser.parse("hello world").isEmpty());
        assertTrue(parser.parse("{\"type\":\"message\"}").isEmpty());
    }

    @Test
    void guardsAgainstInvalidJson() {
        assertTrue(parser.parse("{\"type\":\"call_tool\"").isEmpty());
    }
}
