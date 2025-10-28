package com.example.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

class ToolServiceTests {

    @Test
    void executeCurrentTimeTool() {
        TimeUseCase timeUseCase = mock(TimeUseCase.class);
        when(timeUseCase.currentTime("UTC")).thenReturn("now");

        ToolService service = new ToolService(timeUseCase);
        ToolService.ToolExecution execution = service.execute(new ToolCall("currentTime", Map.of("timezone", "UTC")));

        assertInstanceOf(ToolService.ToolExecution.Result.class, execution);
        assertEquals("now", ((ToolService.ToolExecution.Result) execution).content());
        verify(timeUseCase).currentTime("UTC");
    }

    @Test
    void requestClarificationWhenTimezoneMissing() {
        ToolService service = new ToolService(mock(TimeUseCase.class));

        ToolService.ToolExecution execution = service.execute(new ToolCall("currentTime", Map.of()));

        assertInstanceOf(ToolService.ToolExecution.Clarification.class, execution);
        String card = ((ToolService.ToolExecution.Clarification) execution).card();
        assertTrue(card.contains("提供时区"));
    }

    @Test
    void unknownToolReturnsCard() {
        ToolService service = new ToolService(mock(TimeUseCase.class));

        ToolService.ToolExecution execution = service.execute(new ToolCall("other", Map.of()));

        assertInstanceOf(ToolService.ToolExecution.Clarification.class, execution);
        assertTrue(((ToolService.ToolExecution.Clarification) execution).card().contains("未知工具"));
    }
}
