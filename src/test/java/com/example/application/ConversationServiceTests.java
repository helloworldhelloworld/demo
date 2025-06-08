package com.example.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.chat.prompt.ChatMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.UserMessage;

import com.example.application.PromptService;
import com.example.application.ToolService;

class ConversationServiceTests {

    @Test
    void conversationStoresHistoryPerSession() {
        ChatUseCase useCase = mock(ChatUseCase.class);
        when(useCase.chat(any())).thenReturn("a").thenReturn("b");

        PromptService promptService = mock(PromptService.class);
        when(promptService.buildPrompt(anyList(), anyString()))
                .thenAnswer(inv -> new Prompt(inv.getArgument(0)));

        ToolService toolService = mock(ToolService.class);
        ConversationService service = new ConversationService(useCase, promptService, toolService);
        assertEquals("a", service.chat("1", "hello"));
        assertEquals("b", service.chat("1", "how are you?"));

        ArgumentCaptor<Prompt> captor = ArgumentCaptor.forClass(Prompt.class);
        verify(useCase, times(2)).chat(captor.capture());
        List<Prompt> prompts = captor.getAllValues();
        // first call has only the user message
        assertEquals(List.of(new UserMessage("hello")), prompts.get(0).getMessages());
        // second call should contain first user and assistant plus new user
        List<ChatMessage> second = prompts.get(1).getMessages();
        assertEquals(3, second.size());
        assertEquals("hello", second.get(0).getContent());
        assertEquals("a", second.get(1).getContent());
        assertEquals("how are you?", second.get(2).getContent());
    }
}
