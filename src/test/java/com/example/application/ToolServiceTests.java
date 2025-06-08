package com.example.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

class ToolServiceTests {

    @Test
    void handleDelegatesToTimeUseCase() {
        TimeUseCase timeUseCase = mock(TimeUseCase.class);
        when(timeUseCase.currentTime("UTC")).thenReturn("now");

        ToolService service = new ToolService(timeUseCase);
        assertEquals("now", service.handle("/time UTC"));
        verify(timeUseCase).currentTime("UTC");
    }
}
