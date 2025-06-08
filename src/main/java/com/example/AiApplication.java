package com.example;

import java.util.List;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
    CommandLineRunner demo(ChatUseCase chatUseCase, DocumentUseCase documentUseCase) {
        return args -> {
            documentUseCase.addDocuments(List.of(new Document("1", "Spring AI simplifies AI integration for Spring developers.")));
            var docs = documentUseCase.search("What does Spring AI do?");
            String context = docs.isEmpty() ? "" : docs.get(0).getText();
            var prompt = new Prompt(new UserMessage("Summarize: " + context));
            System.out.println(chatUseCase.chat(prompt));

            var prompt2 = new Prompt(new UserMessage("What time is it?"));
            System.out.println(chatUseCase.chat(prompt2));
        };
    }

    @Bean
    SimpleVectorStore vectorStore(OpenAiApi api) {
        EmbeddingModel model = new OpenAiEmbeddingModel(api);
        return SimpleVectorStore.builder(model).build();
    }
}
