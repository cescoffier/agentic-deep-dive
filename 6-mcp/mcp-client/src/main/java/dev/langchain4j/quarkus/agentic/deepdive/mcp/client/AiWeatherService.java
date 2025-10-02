package dev.langchain4j.quarkus.agentic.deepdive.mcp.client;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.mcp.runtime.McpToolBox;

@RegisterAiService
public interface AiWeatherService {

    @SystemMessage("""
            You are a weather expert. The user will give you a location, and you should first
            get the coordinates for that location, and then based on the coordinates,
            get the weather for that specific location.
            """)
    @McpToolBox
    String getWeather(String message);
}
