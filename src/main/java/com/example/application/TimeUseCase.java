package com.example.application;

import org.springframework.stereotype.Service;

import com.example.domain.TimePort;

@Service
public class TimeUseCase {

    private final TimePort timePort;

    public TimeUseCase(TimePort timePort) {
        this.timePort = timePort;
    }

    public String currentTime(String zone) {
        return timePort.currentTime(zone);
    }
}
