package dev.langchain4j.quarkus.agentic.deepdive.mcp.server.weather;

public record DailyUnits(String time,
                         String temperature_2m_max,
                         String precipitation_sum,
                         String wind_speed_10m_max) {
}
