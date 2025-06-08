package com.example.adapter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.application.ConversationService;

@RestController
public class ChatController {

    private final ConversationService conversationService;

    public ChatController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping("/chat/{sessionId}")
    public ResponseEntity<String> chat(@PathVariable String sessionId, @RequestBody String message) {
        return ResponseEntity.ok(conversationService.chat(sessionId, message));
    }

    @DeleteMapping("/chat/{sessionId}")
    public ResponseEntity<Void> clear(@PathVariable String sessionId) {
        conversationService.clear(sessionId);
        return ResponseEntity.noContent().build();
    }
}
