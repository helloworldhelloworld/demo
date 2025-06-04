package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.UserMessage;

class ChatServiceTests {

    @Test
    void chatReturnsContent() {
        ChatClient client = mock(ChatClient.class);
        ChatClientRequestSpec req = mock(ChatClientRequestSpec.class);
        CallResponseSpec resp = mock(CallResponseSpec.class);

        Prompt prompt = new Prompt(new UserMessage("hello"));

        when(client.prompt(prompt)).thenReturn(req);
        when(req.call()).thenReturn(resp);
        when(resp.content()).thenReturn("hi");

        ChatService service = new ChatService(client);
        assertEquals("hi", service.chat(prompt));
    }
}
