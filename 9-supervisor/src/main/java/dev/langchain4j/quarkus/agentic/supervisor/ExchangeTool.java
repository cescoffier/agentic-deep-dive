package dev.langchain4j.quarkus.agentic.supervisor;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ExchangeTool {

    public static Map<String, Double> exchangeRatesToUSD = new HashMap<>();

    static {
        exchangeRatesToUSD.put("USD", 1.0);
        exchangeRatesToUSD.put("EUR", 1.15);
        exchangeRatesToUSD.put("CHF", 1.25);
        exchangeRatesToUSD.put("CAN", 0.8);
    }

    @Tool("Exchange the given amount of money from the original to the target currency")
    Double exchange(@P("originalCurrency") String originalCurrency, @P("amount") Double amount, @P("targetCurrency") String targetCurrency) {
        Double exchangeRate1 = exchangeRatesToUSD.get(originalCurrency);
        if (exchangeRate1 == null) {
            throw new RuntimeException("No exchange rate found for currency " + originalCurrency);
        }
        Double exchangeRate2 = exchangeRatesToUSD.get(targetCurrency);
        if (exchangeRate2 == null) {
            throw new RuntimeException("No exchange rate found for currency " + targetCurrency);
        }
        return (amount * exchangeRate1) / exchangeRate2;
    }
}