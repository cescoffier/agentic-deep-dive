package org.acme;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;


public class AiServices {
    public static void main(String[] args) {
        Quarkus.run();
    }
    @RegisterAiService
    @RequestScoped
    interface ShortMemoryAssistant {
        String answer(String question);
    }

    @RegisterAiService
    @ApplicationScoped
    interface LongMemoryAssistant {
        String answer(@MemoryId int id, @UserMessage String question);
    }

    @RegisterAiService
    @SessionScoped
    interface ConversationalMemoryAssistant {
        String answer(String question);
    }
}