package com.example.adapter;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import com.example.domain.ChatPort;

@Component
public class OpenAiChatAdapter implements ChatPort {

    private final ChatClient chatClient;

    public OpenAiChatAdapter(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String chat(Prompt prompt) {
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }
}
