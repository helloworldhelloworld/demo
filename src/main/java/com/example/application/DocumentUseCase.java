package com.example.application;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import com.example.domain.DocumentStorePort;

@Service
public class DocumentUseCase {

    private final DocumentStorePort store;

    public DocumentUseCase(DocumentStorePort store) {
        this.store = store;
    }

    public void addDocuments(List<Document> documents) {
        store.addDocuments(documents);
    }

    public List<Document> search(String query) {
        return store.search(query);
    }
}
