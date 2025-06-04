package com.example.adapter;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZoneId;

import org.junit.jupiter.api.Test;

class SystemTimeAdapterTests {

    @Test
    void currentTimeIncludesZone() {
        SystemTimeAdapter adapter = new SystemTimeAdapter();
        String time = adapter.currentTime("UTC");
        assertTrue(time.contains(ZoneId.of("UTC").getId()));
    }
}
