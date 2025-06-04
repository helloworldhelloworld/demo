package com.example.adapter;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.stereotype.Component;

import com.example.domain.DocumentStorePort;

@Component
public class VectorStoreAdapter implements DocumentStorePort {

    private final SimpleVectorStore vectorStore;

    public VectorStoreAdapter(SimpleVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void addDocuments(List<Document> documents) {
        vectorStore.add(documents);
    }

    @Override
    public List<Document> search(String query) {
        return vectorStore.similaritySearch(SearchRequest.query(query));
    }
}
