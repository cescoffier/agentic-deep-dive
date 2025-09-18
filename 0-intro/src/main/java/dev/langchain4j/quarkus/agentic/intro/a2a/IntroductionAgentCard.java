package dev.langchain4j.quarkus.agentic.intro.a2a;

import io.a2a.server.PublicAgentCard;
import io.a2a.spec.AgentCapabilities;
import io.a2a.spec.AgentCard;
import io.a2a.spec.AgentSkill;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class IntroductionAgentCard {

    @Produces
    @PublicAgentCard
    public AgentCard agentCard() {
        return new AgentCard.Builder()
                .name("Introduction Agent")
                .description("Introduce someone")
                .url("http://localhost:8080")
                .version("1.0.0")
                .capabilities(new AgentCapabilities.Builder()
                        .streaming(true)
                        .pushNotifications(false)
                        .stateTransitionHistory(false)
                        .build())
                .defaultInputModes(Collections.singletonList("text"))
                .defaultOutputModes(Collections.singletonList("text"))
                .skills(Collections.singletonList(new AgentSkill.Builder()
                        .id("introduce")
                        .name("Introduce someone")
                        .description("Provide an introduction for a given name")
                        .tags(Collections.singletonList("introduction"))
                        .examples(List.of("introduce John Doe"))
                        .build()))
                .build();
    }
}