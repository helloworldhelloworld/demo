package com.example.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.prompt.AssistantMessage;
import org.springframework.ai.chat.prompt.ChatMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.UserMessage;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {

    private final ChatUseCase chatUseCase;
    private final Map<String, List<ChatMessage>> sessions = new HashMap<>();

    public ConversationService(ChatUseCase chatUseCase) {
        this.chatUseCase = chatUseCase;
    }

    public synchronized String chat(String sessionId, String message) {
        List<ChatMessage> history = sessions.computeIfAbsent(sessionId, id -> new ArrayList<>());
        history.add(new UserMessage(message));
        Prompt prompt = new Prompt(history);
        String response = chatUseCase.chat(prompt);
        history.add(new AssistantMessage(response));
        return response;
    }
}
