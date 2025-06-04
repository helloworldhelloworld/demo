package com.example.application;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import com.example.domain.ChatPort;

@Service
public class ChatUseCase {

    private final ChatPort chatPort;

    public ChatUseCase(ChatPort chatPort) {
        this.chatPort = chatPort;
    }

    public String chat(Prompt prompt) {
        return chatPort.chat(prompt);
    }
}
