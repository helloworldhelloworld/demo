package com.example.adapter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.example.domain.TimePort;

@Component
public class SystemTimeAdapter implements TimePort {

    @Override
    @Tool(name = "currentTime", description = "Get current server time")
    public String currentTime(@ToolParam(description = "Time zone", required = false) String zone) {
        return ZonedDateTime.now(zone == null ? ZoneId.systemDefault() : ZoneId.of(zone)).toString();
    }
}
