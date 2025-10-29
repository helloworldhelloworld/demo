package com.example;

import java.util.List;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.application.ChatUseCase;
import com.example.application.DocumentUseCase;

@SpringBootApplication
@EnableConfigurationProperties(com.example.application.PromptProperties.class)
public class AiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty(value = "app.demo.enabled", havingValue = "true", matchIfMissing = true)
    CommandLineRunner demo(ChatUseCase chatUseCase, DocumentUseCase documentUseCase) {
        return args -> {
            documentUseCase.addDocuments(List.of(Document.builder()
                    .id("1")
                    .text("Spring AI simplifies AI integration for Spring developers.")
                    .build()));
            var docs = documentUseCase.search("What does Spring AI do?");
            String context = docs.isEmpty() ? "" : docs.get(0).getText();
            var prompt = new Prompt(new UserMessage("Summarize: " + context));
            System.out.println(chatUseCase.chat(prompt));

            var prompt2 = new Prompt(new UserMessage("What time is it?"));
            System.out.println(chatUseCase.chat(prompt2));
        };
    }

}
