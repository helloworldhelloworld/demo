package com.example.application;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ToolService {

    public sealed interface ToolExecution permits ToolExecution.Result, ToolExecution.Clarification {

        record Result(String content) implements ToolExecution {
        }

        record Clarification(String card) implements ToolExecution {
        }
    }

    private final TimeUseCase timeUseCase;

    public ToolService(TimeUseCase timeUseCase) {
        this.timeUseCase = timeUseCase;
    }

    public ToolExecution execute(ToolCall call) {
        if ("currentTime".equals(call.name())) {
            return handleCurrentTime(call.arguments());
        }
        return clarification("未知工具", "当前服务未注册工具 " + call.name() + "，请确认名称或选择其他操作。");
    }

    private ToolExecution handleCurrentTime(Map<String, Object> arguments) {
        Object value = arguments.get("timezone");
        if (!(value instanceof String str) || !StringUtils.hasText(str)) {
            return clarification("需要时区信息", "请通过卡片提供IANA格式的时区，例如 \"UTC\" 或 \"Asia/Shanghai\"。");
        }
        String zone = str.trim();
        try {
            return new ToolExecution.Result(timeUseCase.currentTime(zone));
        }
        catch (RuntimeException ex) {
            return clarification("无法识别的时区", "时区 \"" + zone + "\" 无法解析，请检查拼写或改用 UTC。\n错误: " + ex.getMessage());
        }
    }

    private ToolExecution clarification(String title, String body) {
        String card = "{" +
                "\"type\":\"card\"," +
                "\"card\":{" +
                "\"title\":\"" + escape(title) + "\"," +
                "\"body\":\"" + escape(body) + "\"," +
                "\"actions\":[{" +
                "\"type\":\"input\"," +
                "\"name\":\"timezone\"," +
                "\"label\":\"提供时区\"," +
                "\"placeholder\":\"例如 Asia/Shanghai\"},{" +
                "\"type\":\"reply\"," +
                "\"label\":\"使用 UTC\"," +
                "\"value\":\"{\\\"type\\\":\\\"call_tool\\\",\\\"name\\\":\\\"currentTime\\\",\\\"arguments\\\":{\\\"timezone\\\":\\\"UTC\\\"}}\"}]}" +
                "}";
        return new ToolExecution.Clarification(card);
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
