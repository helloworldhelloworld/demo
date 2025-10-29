package com.example.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {

    private final ChatUseCase chatUseCase;
    private final PromptService promptService;
    private final ToolService toolService;
    private final ToolCallParser toolCallParser;
    private final Map<String, List<Message>> sessions = new HashMap<>();

    public ConversationService(ChatUseCase chatUseCase, PromptService promptService, ToolService toolService,
            ToolCallParser toolCallParser) {
        this.chatUseCase = chatUseCase;
        this.promptService = promptService;
        this.toolService = toolService;
        this.toolCallParser = toolCallParser;
    }

    public synchronized String chat(String sessionId, String message) {
        List<Message> history = sessions.computeIfAbsent(sessionId, id -> new ArrayList<>());
        history.add(new UserMessage(message));

        Prompt prompt = promptService.buildPrompt(history, message);
        String response = chatUseCase.chat(prompt);
        return handleAssistantResponse(history, response);
    }

    private String handleAssistantResponse(List<Message> history, String response) {
        return toolCallParser.parse(response)
                .map(toolService::execute)
                .map(execution -> appendToolExecution(history, execution))
                .orElseGet(() -> {
                    history.add(new AssistantMessage(response));
                    return response;
                });
    }

    private String appendToolExecution(List<Message> history, ToolService.ToolExecution execution) {
        if (execution instanceof ToolService.ToolExecution.Result result) {
            String content = result.content();
            history.add(new AssistantMessage(content));
            return content;
        }
        ToolService.ToolExecution.Clarification clarification = (ToolService.ToolExecution.Clarification) execution;
        String card = clarification.card();
        history.add(new AssistantMessage(card));
        return card;
    }

    public synchronized void clear(String sessionId) {
        sessions.remove(sessionId);
    }
}
