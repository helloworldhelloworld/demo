package com.example.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;

class RagServiceTests {

    @Test
    void addAndSearchDelegatesToStore() {
        SimpleVectorStore store = mock(SimpleVectorStore.class);
        RagService service = new RagService(store);

        List<Document> docs = Collections.singletonList(new Document("id", "text"));
        service.addDocuments(docs);
        verify(store).add(docs);

        when(store.similaritySearch(SearchRequest.query("q"))).thenReturn(docs);
        assertSame(docs, service.search("q"));
    }
}
