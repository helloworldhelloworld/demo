package com.example.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.prompt.ChatMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemMessage;
import org.springframework.ai.chat.prompt.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

@Service
public class PromptService {

    private final DocumentUseCase documentUseCase;

    public PromptService(DocumentUseCase documentUseCase) {
        this.documentUseCase = documentUseCase;
    }

    public Prompt buildPrompt(List<ChatMessage> history, String userMessage) {
        List<ChatMessage> messages = new ArrayList<>(history);
        messages.add(new UserMessage(userMessage));

        List<Document> docs = documentUseCase.search(userMessage);
        if (!docs.isEmpty()) {
            messages.add(new SystemMessage("Context: " + docs.get(0).getText()));
        }

        return new Prompt(messages);
    }
}
