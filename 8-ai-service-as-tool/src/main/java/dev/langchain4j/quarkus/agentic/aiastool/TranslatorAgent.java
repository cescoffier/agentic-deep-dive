package dev.langchain4j.quarkus.agentic.aiastool;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService
@ApplicationScoped
public interface TranslatorAgent {

    @SystemMessage("You are a helpful AI translator that translates text to the specified language.")
    @UserMessage("Translate in {language} the following text: '{text}'")
    @Tool("Translate a text to the specified language")
    String translate(String text, String language);
}
