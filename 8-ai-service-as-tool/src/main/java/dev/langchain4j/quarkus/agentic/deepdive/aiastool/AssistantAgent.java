package dev.langchain4j.quarkus.agentic.deepdive.aiastool;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.ToolBox;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService
@ApplicationScoped
public interface AssistantAgent {

    @SystemMessage("You are a kind AI assistant that helps people find information.")
    @UserMessage("The user request is: '{request}'")
    @ToolBox(TranslatorAgent.class)
    String assist(String request);
}
