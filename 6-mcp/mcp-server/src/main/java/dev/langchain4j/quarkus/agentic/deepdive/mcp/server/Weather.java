package dev.langchain4j.quarkus.agentic.deepdive.mcp.server;

import dev.langchain4j.quarkus.agentic.deepdive.mcp.server.geo.GeoCodingService;
import dev.langchain4j.quarkus.agentic.deepdive.mcp.server.geo.GeoResult;
import dev.langchain4j.quarkus.agentic.deepdive.mcp.server.weather.WeatherForecast;
import dev.langchain4j.quarkus.agentic.deepdive.mcp.server.weather.WeatherForecastService;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class Weather {

    @RestClient
    GeoCodingService geoCodingService;

    @RestClient
    WeatherForecastService weatherForecastService;

    @Tool(description = "Finds the latitude and longitude of a given city")
    GeoResult geoLocate(@ToolArg(description = "The city") String city) {
        return geoCodingService.findCity(city).getFirst();
    }

    @Tool(description = "Forecasts the weather for the given latitude and longitude")
    WeatherForecast geoWeatherForecast(@ToolArg(description = "latitude") double latitude, @ToolArg(description = "longitude") double longitude) {
        return weatherForecastService.forecast(latitude, longitude);
    }

    @Tool(description = "Forecasts the weather for the given city")
    WeatherForecast cityWeatherForecast(@ToolArg(description = "The city") String city) {
        GeoResult geoResult = geoCodingService.findCity(city).getFirst();
        return weatherForecastService.forecast(geoResult.latitude(), geoResult.longitude());
    }
}
