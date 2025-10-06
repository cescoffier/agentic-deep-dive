package org.acme;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import dev.langchain4j.service.guardrail.OutputGuardrails;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface Assistant {

    @InputGuardrails(UppercaseInputGuardrail.class)
    @OutputGuardrails(UppercaseOutputGuardrail.class)
    String chat(@UserMessage String userMessage);
}
