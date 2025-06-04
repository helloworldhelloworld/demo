package com.example.domain;

import java.util.List;

import org.springframework.ai.document.Document;

public interface DocumentStorePort {
    void addDocuments(List<Document> documents);
    List<Document> search(String query);
}
