package com.example.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.example.domain.DocumentStorePort;

/**
 * Simple in-memory vector store using {@link LocalVectorizer}.
 */
@Component
@Primary
public class LocalVectorStoreAdapter implements DocumentStorePort {

    private final Map<String, Document> documents = new HashMap<>();
    private final Map<String, double[]> vectors = new HashMap<>();
    private final LocalVectorizer vectorizer = new LocalVectorizer();

    @Override
    public synchronized void addDocuments(List<Document> docs) {
        for (Document d : docs) {
            documents.put(d.getId(), d);
            vectors.put(d.getId(), vectorizer.vectorize(d.getText()));
        }
    }

    @Override
    public synchronized List<Document> search(String query) {
        double[] qv = vectorizer.vectorize(query);
        String bestId = null;
        double best = -1.0;
        for (Map.Entry<String, double[]> e : vectors.entrySet()) {
            double score = vectorizer.cosineSimilarity(qv, e.getValue());
            if (score > best) {
                best = score;
                bestId = e.getKey();
            }
        }
        if (bestId == null) {
            return List.of();
        }
        return List.of(documents.get(bestId));
    }
}
