package dev.langchain4j.quarkus.agentic.intro.a2a;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService
@ApplicationScoped
public interface IntroductionAgent {

    @UserMessage("{request}, Answer in 50 words or less.")
    String introduce(String request);
}
