package com.example.domain;

import org.springframework.ai.chat.prompt.Prompt;

public interface ChatPort {
    String chat(Prompt prompt);
}
