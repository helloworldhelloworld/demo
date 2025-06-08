package com.example.application;

import org.springframework.stereotype.Service;

@Service
public class ToolService {

    private final TimeUseCase timeUseCase;

    public ToolService(TimeUseCase timeUseCase) {
        this.timeUseCase = timeUseCase;
    }

    public String handle(String message) {
        if (message.startsWith("/time")) {
            String[] parts = message.split("\\s+", 2);
            String zone = parts.length > 1 ? parts[1] : null;
            return timeUseCase.currentTime(zone);
        }
        return null;
    }
}
