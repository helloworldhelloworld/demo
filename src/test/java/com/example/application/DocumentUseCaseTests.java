package com.example.application;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;

import com.example.domain.DocumentStorePort;

class DocumentUseCaseTests {

    @Test
    void addAndSearchDelegatesToStore() {
        DocumentStorePort store = mock(DocumentStorePort.class);
        DocumentUseCase useCase = new DocumentUseCase(store);

        List<Document> docs = Collections.singletonList(new Document("id", "text"));
        useCase.addDocuments(docs);
        verify(store).addDocuments(docs);

        when(store.search("q")).thenReturn(docs);
        assertSame(docs, useCase.search("q"));
    }
}
