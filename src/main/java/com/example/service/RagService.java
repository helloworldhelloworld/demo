package com.example.service;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.stereotype.Service;

@Service
public class RagService {

    private final SimpleVectorStore vectorStore;

    public RagService(SimpleVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void addDocuments(List<Document> documents) {
        vectorStore.add(documents);
    }

    public List<Document> search(String query) {
        return vectorStore.similaritySearch(SearchRequest.query(query));
    }
}
