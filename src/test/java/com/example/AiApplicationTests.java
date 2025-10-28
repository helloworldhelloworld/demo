package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = { "spring.ai.openai.api-key=test", "app.demo.enabled=false" })
class AiApplicationTests {

    @MockBean
    ChatClient chatClient;

    @Test
    void contextLoads() {
    }
}
