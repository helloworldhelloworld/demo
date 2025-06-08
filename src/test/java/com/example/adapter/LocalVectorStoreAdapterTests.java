package com.example.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;

class LocalVectorStoreAdapterTests {

    @Test
    void searchReturnsMostSimilarDocument() {
        LocalVectorStoreAdapter store = new LocalVectorStoreAdapter();
        store.addDocuments(List.of(new Document("1", "hello world"), new Document("2", "goodbye")));
        var result = store.search("hello");
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
    }
}
