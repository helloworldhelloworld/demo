package com.example.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

import com.fasterxml.jackson.databind.ObjectMapper;

class ConversationServiceTests {

    @Test
    void conversationStoresHistoryPerSession() {
        ChatUseCase useCase = mock(ChatUseCase.class);
        when(useCase.chat(any())).thenReturn("a").thenReturn("b");

        PromptService promptService = mock(PromptService.class);
        when(promptService.buildPrompt(anyList(), anyString())).thenAnswer(inv -> {
            @SuppressWarnings("unchecked")
            List<Message> messages = (List<Message>) inv.getArgument(0);
            return new Prompt(List.copyOf(messages));
        });

        ToolService toolService = mock(ToolService.class);
        ConversationService service = new ConversationService(useCase, promptService, toolService,
                new ToolCallParser(new ObjectMapper()));
        assertEquals("a", service.chat("1", "hello"));
        assertEquals("b", service.chat("1", "how are you?"));

        ArgumentCaptor<Prompt> captor = ArgumentCaptor.forClass(Prompt.class);
        verify(useCase, times(2)).chat(captor.capture());
        List<Prompt> prompts = captor.getAllValues();
        // first call has only the user message
        assertEquals(List.of(new UserMessage("hello")), prompts.get(0).getInstructions());
        // second call should contain first user and assistant plus new user
        List<Message> second = prompts.get(1).getInstructions();
        assertEquals(3, second.size());
        assertEquals("hello", second.get(0).getText());
        assertEquals("a", second.get(1).getText());
        assertEquals("how are you?", second.get(2).getText());
    }

    @Test
    void automaticallyExecutesToolCall() {
        ChatUseCase useCase = mock(ChatUseCase.class);
        when(useCase.chat(any())).thenReturn("{\"type\":\"call_tool\",\"name\":\"currentTime\",\"arguments\":{\"timezone\":\"UTC\"}} ");

        PromptService promptService = mock(PromptService.class);
        when(promptService.buildPrompt(anyList(), anyString())).thenReturn(new Prompt(List.of()));

        ToolService toolService = mock(ToolService.class);
        when(toolService.execute(any())).thenReturn(new ToolService.ToolExecution.Result("now"));

        ConversationService service = new ConversationService(useCase, promptService, toolService,
                new ToolCallParser(new ObjectMapper()));

        assertEquals("now", service.chat("session", "time"));
        verify(toolService).execute(argThat(call -> call.name().equals("currentTime")));
    }

    @Test
    void returnsClarificationCardWhenToolNeedsInput() {
        ChatUseCase useCase = mock(ChatUseCase.class);
        when(useCase.chat(any())).thenReturn("{\"type\":\"call_tool\",\"name\":\"currentTime\",\"arguments\":{}}");

        PromptService promptService = mock(PromptService.class);
        when(promptService.buildPrompt(anyList(), anyString())).thenReturn(new Prompt(List.of()));

        ToolService toolService = mock(ToolService.class);
        when(toolService.execute(any())).thenReturn(new ToolService.ToolExecution.Clarification("{\"type\":\"card\"}"));

        ConversationService service = new ConversationService(useCase, promptService, toolService,
                new ToolCallParser(new ObjectMapper()));

        String response = service.chat("session", "time");
        assertEquals("{\"type\":\"card\"}", response);
    }
}
