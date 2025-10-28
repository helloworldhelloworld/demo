package com.example.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;

class LocalVectorStoreAdapterTests {

    @Test
    void searchReturnsMostSimilarDocument() {
        LocalVectorStoreAdapter store = new LocalVectorStoreAdapter();
        store.addDocuments(List.of(Document.builder().id("1").text("hello world").build(),
                Document.builder().id("2").text("goodbye").build()));
        var result = store.search("hello");
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
    }
}
