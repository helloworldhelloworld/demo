package com.example.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZoneId;

import org.junit.jupiter.api.Test;

class TimeServiceTests {

    @Test
    void currentTimeIncludesZone() {
        TimeService svc = new TimeService();
        String time = svc.currentTime("UTC");
        assertTrue(time.contains(ZoneId.of("UTC").getId()));
    }
}
