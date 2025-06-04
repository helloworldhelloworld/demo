package com.example;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(ChatClient chatClient, SimpleVectorStore vectorStore) {
        return args -> {
            vectorStore.add(List.of(new Document("1", "Spring AI simplifies AI integration for Spring developers.")));
            var docs = vectorStore.similaritySearch(SearchRequest.query("What does Spring AI do?"));
            String context = docs.isEmpty() ? "" : docs.get(0).getText();
            var prompt = new Prompt(new UserMessage("Summarize: " + context));
            System.out.println(chatClient.call(prompt).getResult().getOutput().getContent());

            var prompt2 = new Prompt(new UserMessage("What time is it?"));
            System.out.println(chatClient.call(prompt2).getResult().getOutput().getContent());
        };
    }

    @Tool(name = "currentTime", description = "Get current server time")
    public String currentTime(@ToolParam(description = "Time zone", required = false) String zone) {
        return ZonedDateTime.now(zone == null ? ZoneId.systemDefault() : ZoneId.of(zone)).toString();
    }

    @Bean
    SimpleVectorStore vectorStore(OpenAiApi api) {
        EmbeddingModel model = new OpenAiEmbeddingModel(api);
        return SimpleVectorStore.builder(model).build();
    }
}
