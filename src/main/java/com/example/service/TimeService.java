package com.example.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class TimeService {

    @Tool(name = "currentTime", description = "Get current server time")
    public String currentTime(@ToolParam(description = "Time zone", required = false) String zone) {
        return ZonedDateTime.now(zone == null ? ZoneId.systemDefault() : ZoneId.of(zone)).toString();
    }
}
