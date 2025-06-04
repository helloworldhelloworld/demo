package com.example.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.UserMessage;

import com.example.domain.ChatPort;

class ChatUseCaseTests {

    @Test
    void chatDelegatesToPort() {
        ChatPort port = mock(ChatPort.class);
        Prompt prompt = new Prompt(new UserMessage("hello"));
        when(port.chat(prompt)).thenReturn("hi");

        ChatUseCase useCase = new ChatUseCase(port);
        assertEquals("hi", useCase.chat(prompt));
    }
}
